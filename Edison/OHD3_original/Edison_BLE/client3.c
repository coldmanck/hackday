#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/rfcomm.h>

int main(int argc, char **argv)
{
    struct sockaddr_rc addr = { 0 };
    int s, status, count = 0;
    char dest[18] = "74:E5:43:0A:F0:50";

    // allocate a socket
    s = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);

    // set the connection parameters (who to connect to)
    addr.rc_family = AF_BLUETOOTH;
    addr.rc_channel = (uint8_t) 1;
    str2ba( dest, &addr.rc_bdaddr );

    status = connect(s, (struct sockaddr *)&addr, sizeof(addr));
    if(status < 0){
        fprintf(stderr, "connect failed\n");
        return 1;
    }

    while(1){
        printf("on connection. %d\n", count++);
        status = write(s, "123", 3);
        if(status < 0)break;
        sleep(1);
    }
    // send mail

    system("send.rb coldmanck");
    // when status = -1
    if( status < 0 ) perror("uh oh");

    close(s);
    return 0;
}
