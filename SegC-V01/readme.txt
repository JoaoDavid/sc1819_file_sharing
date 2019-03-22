Como executar o projecto

1)Preparar os run configurations dos dois programas

--- Para o MsgFile ---
Program arguments:
127.0.0.1:23456 <username> <passwd>
exemplo:
127.0.0.1:23456 fernando pass123

VM arguments:
-Djava.security.manager -Djava.security.policy=client.policy


--- Para o MsgFileServer ---
Program arguments:
23456

VM arguments:
-Djava.security.manager -Djava.security.policy=server.policy

2)Correr o MsgFileServer (server)

3)Correr o MsgFile (client)

4)Usar o MsgFile para introduzir os comandos, usar "help"
para ver lista de comandos existentes no programa
