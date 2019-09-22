#!/usr/bin/env bash

pwd  # should be in the /.../shared folder on the host
ls  # should show make_lots_of_connections_to_servers.py, connections_singlenode.cfg
cd ..
vagrant ssh

mongod -f /shared/connections_singlenode.cfg
lsof -i:27000

python make_lots_of_connections_to_servers.py --help

 # terminal 2
vagrant ssh

mongostat --port 27000 

mongostat --port 27000 -o "command,dirty,used,vsize,res,conn,time"

# back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 100

# look at mongostat in terminal 2
# Back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 200

# look at mongostat in terminal 2
# Back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 600

# terminal 2
# look at mongostat
# ^c to close mongostat

tail -F /home/vagrant/conns_single.log

# back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 600

# look at tail of logs in terminal 2
# back to terminal 1

less /shared/connections_singlenode.cfg

sed -ie 's%maxIncomingConnections: 200%%' /shared/connections_singlenode.cfg

mongo --port 27000

# back to terminal 2

mongod -f /shared/connections_singlenode.cfg

mongostat --port 27000 -o "command,dirty,used,vsize,res,conn,time"

# back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 600

free -h

watch -n 2 free -h

# terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 600

# look at free in terminal 2

# back to terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 2000  # you'll run out of memory, most likely

# look at free in terminal 2

# back to terminal 1
# look at free in terminal 2

ulimit -a

# terminal 1
mongo --port 27000  # second connection

ulimit -n 2048
mongod -f /shared/connections_singlenode.cfg

mongo --port 27000  # third connection

# After this, kill the server.
echo 'db.shutdownServer()' | mongo --port 27000 admin

mkdir connsrepl
mlaunch --port 27000 --name CONNS --replicaset --dir connsrepl

# terminal 2

mongostat --discover --port 27000 -o "command,dirty,used,vsize,res,conn,time,repl"

# terminal 1

python /shared/make_lots_of_connections_to_servers.py --port 27000 -n 600 --repl CONNS

# terminal 2
# ^c to exit mongostat
mongo --port 27000 --eval 'rs.isMaster().primary'
mongo --port 27000 --eval 'rs.stepDown()'

# terminal 1

edit /shared/make_lots_of_connections_to_servers.py
