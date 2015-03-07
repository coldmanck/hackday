#include <stdio.h>
#include <unistd.h>
#include <sys/socket.h>
#include <bluetooth/bluetooth.h>
#include <bluetooth/rfcomm.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
int colon_to_dash(char*, char*);
int send_query(char*, char*, int);
int main(int argc, char **argv){
    struct sockaddr_rc addr = { 0 };
    int s, status, count = 0;
    char target_id[32];
    char edison_id[32] = "98:4F:EE:03:93:37";
    if(argc < 2){
        fprintf(stderr, "no target id input.\n");
        return 1;
    } else {
        strcpy(target_id, argv[1]);
    }
    while(1){
        while(1){
            s = socket(AF_BLUETOOTH, SOCK_STREAM, BTPROTO_RFCOMM);
            addr.rc_family = AF_BLUETOOTH;
            addr.rc_channel = (uint8_t) 1;
            str2ba(target_id, &addr.rc_bdaddr );
            status = connect(s, (struct sockaddr *)&addr, sizeof(addr));
            printf("trying to connect %s\n", target_id);
            if(status >= 0){
                printf("connected.\n");
                send_query(edison_id, target_id, 1);
                break;
            }
            sleep(1);
        }
        while(1){
            status = write(s, "test", 4);
            printf("connecting.\n");
            if(status < 0){
                printf("disconnected %s.\n", target_id);
                send_query(edison_id, target_id, 0);
                close(s);
                break;
            }
            sleep(1);
        }
    }
    return 0;
}

int send_query(char *edison_id, char *target_id, int d_on){
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[1024];

    portno = 80;
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) fprintf(stderr, "ERROR opening socket.\n");
    server = gethostbyname("jp.nagi.tw");
    if (server == NULL){fprintf(stderr,"ERROR, no such host\n");}
    
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr, server->h_length);
    serv_addr.sin_port = htons(portno);
    if(connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){fprintf(stderr, "ERROR connecting.\n");}
    
    char buf2[32], buf3[32];
    colon_to_dash(edison_id, buf2);
    colon_to_dash(target_id, buf3);
    
    sprintf(buffer, "GET /notify.php?edison_id=%s&target_id=%s&con=%d HTTP/1.0\r\n\r\n", buf2, buf3, d_on);

    // printf("%s\n", buffer);
    n = write(sockfd, buffer, strlen(buffer));
    if (n < 0) error("ERROR writing to socket");
    
    bzero(buffer, 256);
    // n = read(sockfd,buffer,255);
    // if (n < 0) error("ERROR reading from socket");
    
    close(sockfd);
    return 0;
}
int colon_to_dash(char *s, char *buf){
    int i;
    strcpy(buf, s);
    for(i=0; i<5; i++){
        buf[i*3 + 2] = '_';
    }
}
