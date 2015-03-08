package com.theta360.lib.rexif;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.theta360.lib.rexif.entity.EntryField;
import com.theta360.lib.rexif.entity.ImageFileDirectory;
import com.theta360.lib.rexif.entity.JpegImageData;
import com.theta360.lib.rexif.entity.OmniInfo;
import com.theta360.lib.rexif.util.DataProcessingUtil;
import com.theta360.lib.rexif.util.FieldDataAnalizer;

/**
 * EXIF read utility
 * 
 */
public class ExifReader {

	/**
	 * SOI (start of image) marker
	 */
	public final static int EXIF_HEADER_JPEG_SOI = 0xFFD8;
	/**
	 * APP0 marker
	 */
	public final static int EXIF_HEADER_JPEG_APP0 = 0xFFE0;
	/**
	 * APP1 marker
	 */
	public final static int EXIF_HEADER_JPEG_APP1 = 0xFFE1;
	/**
	 * SOS (start of stream) marker
	 */
	public static final int EXIF_HEADER_JPEG_SOS = 0xFFDA;
	/**
	 * Byte offset at starting point of IFD offset (TIFF header).
	 * Referencing of the IFD offset position references the IFD offset value plus this value.  SOI (2 bytes) + APP1 marker (2 bytes) +
	 * APP1 size (2 bytes) + Exif identification code (6 bytes) = 12 bytes
	 */
	public final static int EXIF_HEADER_OFFSET = 12;

	private final static int BIT_SIZE_8 = 8;
	private final static int BIT_SIZE_16 = 16;
	private final static int BIT_SIZE_24 = 24;

	private static final int EXIF_MAX_SIZE = 64000;
	private static final int EXIF_APP1_HEADER_SIZE = 18;
	private int restExifSize = EXIF_MAX_SIZE - EXIF_APP1_HEADER_SIZE;
	private static final int ONE_IFD_SIZE = 12;

	// Marker & Tiff Header Size Structure
	private static final int LENGTH_SIZE = 2;
	private static final int EXIF_CODE_SIZE = 6;
	private static final int TIFF_HEADER_SIZE = 8;
	// IDF Size Structure
	private static final int IFD_NEXT_OFFSET_SIZE = 4;
	// EntryField Size Structure
	private static final int FIELD_OFFSET_SIZE = 4;

	private static final int RICOH_CAMERA_TAG_SIZE = 6;
	private static final int MAKER_NOTE_DUMMY_DATA_SIZE = 2;
	private static final String RICOH_CAMERA_TAG_VALUE = "Ricoh\0";

	private FileInputStream fis;

	private ImageFileDirectory zerothIfd = null;
	private ImageFileDirectory exifIfd = null;
	private ImageFileDirectory firstIfd = null;
	private ImageFileDirectory makerNoteIfd = null;
	private OmniInfo omniInfo = null;
	private JpegImageData thumbnail = null;

	/**
	 * Reads the data of the JPEG file specified as an argument.
	 * 
	 * @param file
	 *            The JPEG file to be read
	 * @throws IOException
	 *             Reading of the file failed
	 * @throws ExifReadException
	 *             Reading of the EXIF data failed
	 */
	public ExifReader(File file) throws IOException, ExifReadException {

		fis = new FileInputStream(file);
		int soi = read2Byte();
		if (soi != EXIF_HEADER_JPEG_SOI) {
			throw new ExifReadException("Unmatch JPEG SOI:" + Long.toHexString(soi));
		}
		try {
			int app1Marker = read2Byte();
			if (app1Marker != EXIF_HEADER_JPEG_APP1) {
				throw new ExifReadException("Unmatch JPEG APP1 MARKER:" + app1Marker);
			}
			fis.skip(LENGTH_SIZE);
			fis.skip(EXIF_CODE_SIZE);
			fis.skip(TIFF_HEADER_SIZE);

			int entryFieldCount = read2Byte();
			checkExifOverSize(entryFieldCount);
			byte[] nextOffset;
			zerothIfd = new ImageFileDirectory(entryFieldCount);
			zerothIfd.setEntryFieldList(createEntryFieldList(entryFieldCount));
			nextOffset = readByteArray(IFD_NEXT_OFFSET_SIZE);
			zerothIfd.setNextOffset(nextOffset);

			EntryField exifIfdEntryField = zerothIfd.getEntryField(ImageFileDirectory.ENTRY_FIELD_TAG_EXIF);
			if (exifIfdEntryField == null) {
				throw new ExifReadException("No Exif Entry Field");
			}
			long skipByte = DataProcessingUtil.byteToLong(exifIfdEntryField.getOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
			fis.skip(skipByte);
			entryFieldCount = read2Byte();
			checkExifOverSize(entryFieldCount);
			exifIfd = new ImageFileDirectory(entryFieldCount);
			exifIfd.setEntryFieldList(createEntryFieldList(entryFieldCount));
			nextOffset = readByteArray(IFD_NEXT_OFFSET_SIZE);
			exifIfd.setNextOffset(nextOffset);

			EntryField makernoteEntryField = exifIfd.getEntryField(ImageFileDirectory.ENTRY_FIELD_TAG_MAKERNOTE);
			if (makernoteEntryField == null) {
				throw new ExifReadException("No Makernote Entry Field");
			}
			skipByte = DataProcessingUtil.byteToLong(makernoteEntryField.getOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
			fis.skip(skipByte);
			byte[] ricohCameraTag = readByteArray(RICOH_CAMERA_TAG_SIZE);
			if (!RICOH_CAMERA_TAG_VALUE.equals(new String(ricohCameraTag))) {
				makerNoteIfd = null;
			} else {
				fis.skip(MAKER_NOTE_DUMMY_DATA_SIZE);
				entryFieldCount = read2Byte();
				checkExifOverSize(entryFieldCount);
				makerNoteIfd = new ImageFileDirectory(entryFieldCount);
				makerNoteIfd.setEntryFieldList(createEntryFieldList(entryFieldCount));
				nextOffset = readByteArray(IFD_NEXT_OFFSET_SIZE);
				makerNoteIfd.setNextOffset(nextOffset);
			}

			if (makerNoteIfd != null) {
				EntryField sphereInfoEntryField = makerNoteIfd.getEntryField(ImageFileDirectory.ENTRY_FIELD_TAG_SPHERE_INFO);
				if (sphereInfoEntryField == null) {
					throw new ExifReadException("No Sphere Info Entry Field");
				}
				skipByte = DataProcessingUtil.byteToLong(sphereInfoEntryField.getOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
				fis.skip(skipByte);
				entryFieldCount = read2Byte();
				checkExifOverSize(entryFieldCount);
				ImageFileDirectory spherePhotoInfoIfd = new ImageFileDirectory(entryFieldCount);
				ArrayList<EntryField> entryFieldList = createEntryFieldList(entryFieldCount);
				nextOffset = readByteArray(IFD_NEXT_OFFSET_SIZE);
				Collections.sort(entryFieldList, asc);
				for (EntryField entryField : entryFieldList) {
					if (FieldDataAnalizer.isImmediate(entryField)) {
						entryField.setValue(entryField.getOffset());
					} else {
						skipByte = DataProcessingUtil.byteToLong(entryField.getOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
						fis.skip(skipByte);
						entryField.setValue(readByteArray(FieldDataAnalizer.getDataLength(entryField)));
					}
				}
				spherePhotoInfoIfd.setEntryFieldList(entryFieldList);
				spherePhotoInfoIfd.setNextOffset(nextOffset);

				omniInfo = new OmniInfo();
				for (EntryField entryField : entryFieldList) {
					if (entryField.getTag() == ImageFileDirectory.ENTRY_FIELD_TAG_INCLINATION_INFO) {
						double[] inclinations = FieldDataAnalizer.convertValueToSRational(entryField);
						omniInfo.setHorizontalAngle(inclinations[0]);
						omniInfo.setElevationAngle(inclinations[1]);
					} else if (entryField.getTag() == ImageFileDirectory.ENTRY_FIELD_TAG_COMPASS_INFO) {
						double[] orientation = FieldDataAnalizer.convertValueToRational(entryField);
						omniInfo.setOrientationAngle(orientation[0]);
					}
				}
			}

			skipByte = DataProcessingUtil.byteToLong(zerothIfd.getNextOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
			fis.skip(skipByte);
			entryFieldCount = read2Byte();
			firstIfd = new ImageFileDirectory(entryFieldCount);
			firstIfd.setEntryFieldList(createEntryFieldList(entryFieldCount));
			nextOffset = readByteArray(IFD_NEXT_OFFSET_SIZE);

			EntryField thumbSoiEntryField = firstIfd.getEntryField(ImageFileDirectory.ENTRY_FIELD_TAG_THUMB_SOI);
			if (thumbSoiEntryField == null) {
				throw new ExifReadException("No Thumb SOI Entry Field");
			}
			skipByte = DataProcessingUtil.byteToLong(thumbSoiEntryField.getOffset(), 0) - fis.getChannel().position() + EXIF_HEADER_OFFSET;
			fis.skip(skipByte);
			thumbnail = new JpegImageData();
			EntryField thumbSizeEntryField = firstIfd.getEntryField(ImageFileDirectory.ENTRY_FIELD_TAG_THUMB_SIZE);
			if (thumbSizeEntryField == null) {
				throw new ExifReadException("No Thumb Size Entry Field");
			}
			thumbnail.setData(readByteArray((int) DataProcessingUtil.byteToLong(thumbSizeEntryField.getOffset(), 0)));
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				fis = null;
				return;
			}
		}
	}

	Comparator<EntryField> asc = new Comparator<EntryField>() {
		public int compare(EntryField fieldA, EntryField fieldB) {
			long offsetA = DataProcessingUtil.byteToLong(((EntryField) fieldA).getOffset(), 0);
			long offsetB = DataProcessingUtil.byteToLong(((EntryField) fieldB).getOffset(), 0);
			int ret = 0;
			if (offsetA == offsetB) {
				long tagA = ((EntryField) fieldA).getTag();
				long tagB = ((EntryField) fieldB).getTag();
				ret = tagA < tagB ? -1 : 1;
			} else {
				ret = offsetA < offsetB ? -1 : 1;
			}
			return ret;
		}
	};

	private void checkExifOverSize(int entryFieldCount) throws ExifReadException {
		restExifSize = restExifSize - (entryFieldCount * ONE_IFD_SIZE);
		if (restExifSize < 0) {
			throw new ExifReadException("There is no longer rest Exif Size. fieldCount:" + entryFieldCount);
		}
	}

	private ArrayList<EntryField> createEntryFieldList(int entryFieldCount) throws IOException {
		ArrayList<EntryField> entryFieldList = new ArrayList<EntryField>();
		for (int i = 0; i < entryFieldCount; i++) {
			int tag = read2Byte();
			byte[] offset;
			EntryField entryField = new EntryField();
			entryField.setTag(tag);
			entryField.setType(read2Byte());
			entryField.setCount(read4Byte());
			offset = readByteArray(FIELD_OFFSET_SIZE);
			entryField.setOffset(offset);
			entryFieldList.add(entryField);
		}

		return entryFieldList;
	}

	private int read2Byte() throws IOException {
		int readData = 0;
		readData = fis.read() << BIT_SIZE_8 | fis.read();
		return readData;
	}

	private int read4Byte() throws IOException {
		int readData = 0;
		readData = fis.read() << BIT_SIZE_24 | fis.read() << BIT_SIZE_16 | fis.read() << BIT_SIZE_8 | fis.read();
		return readData;
	}

	private byte[] readByteArray(int size) throws IOException {
		byte[] buf = new byte[size];
		fis.read(buf, 0, size);
		return buf;
	}

	/**
	 * Acquire ExifIFD
	 * 
	 * @return ExifIFD object
	 */
	public ImageFileDirectory getExifIfd() {
		return exifIfd;
	}

	/**
	 * Acquires spherical image information
	 * 
	 * @return Spherical image information object
	 */
	public OmniInfo getOmniInfo() {
		return omniInfo;
	}

	/**
	 * Acquires thumbnail in Exif
	 * 
	 * @return Thumbnail image information
	 */
	public JpegImageData getThumbnail() {
		return thumbnail;
	}

}
