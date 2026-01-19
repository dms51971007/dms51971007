туннель
ssh -p 22 -L 127.0.0.1:33306:127.0.0.1:3306 root@turchenkov1.fvds.ru

томкат
http://turchenkov1.fvds.ru:8080/manager/html

деплой
curl -u admin:liek2dec3i -X PUT \ --data-binary @signalm-manager-new.war \ "http://turchenkov1.fvds.ru:8080/manager/text/deploy?path=/signalm-manager-new&update=true"