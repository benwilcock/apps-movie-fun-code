cf create-service p-mysql 100mb movies-mysql

cf bind-service moviefun movies-mysql

cf restage moviefun
