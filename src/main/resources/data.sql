insert into _user (ID,USER_NAME,PASSWORD,EMAIL) VALUES (1000,'root','admin123','root@gmail.com') ;
insert into _user (ID,USER_NAME,PASSWORD,EMAIL) VALUES (1001,'admin','admin','admin@gmail.com') ;
insert into _task (TITLE,DESCRIPTION,COMPLETE,USER_ID) VALUES ('Primera Tarea','Descriptcion',false,1000) ;
insert into _task (TITLE,DESCRIPTION,COMPLETE,USER_ID) VALUES ('Segunda Tarea','Descriptcion',false,1000) ;
insert into _task (TITLE,DESCRIPTION,COMPLETE,USER_ID) VALUES ('Tercera Tarea','Descriptcion',false,1000) ;
insert into _task (TITLE,DESCRIPTION,COMPLETE,USER_ID) VALUES ('Primera Tarea Admin','Descriptcion',false,1001) ;