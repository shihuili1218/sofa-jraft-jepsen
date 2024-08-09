unset SSH_AUTH_SOCK
lein run test --username admin --password 123456 --nodes-file  ./nodes  --ssh-private-key /home/admin/.ssh/id_rsa $@
