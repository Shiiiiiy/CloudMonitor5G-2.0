#!/bin/bash
#filename deploy-api.sh
set -x
#export JAVA_HOME=/opt/jdk1.7.0_80
export TOMCAT_HOME=/home/iads/tomcat8/apache-tomcat-8.0.33
api_pid=$(ps -ef | grep -v grep | grep -i '/home/iads/tomcat8/apache-tomcat-8.0.33/' | awk '{print $2}')
for temp_pid in ${api_pid}
do 
    kill -9 ${temp_pid}
done
war_file="/home/iads/tomcat8/CloudMonitor5G.war"
if [ -f "$war_file" ];
then
    echo "War file exists, deploy and start the server."
    
    cd /home/iads/tomcat8/back_up
	back_dir=CloudMonitor5G_`date +%F_%T`
    mkdir -p $back_dir
    cp -rf ${TOMCAT_HOME}/webapps/CloudMonitor5G /home/iads/tomcat8/back_up/$back_dir
	echo "backup CloudMonitor5G, /home/iads/tomcat8/back_up/"$back_dir
    
    rm -rf ${TOMCAT_HOME}/webapps/CloudMonitor5G/
    rm -f ${TOMCAT_HOME}/webapps/CloudMonitor5G.war
    mv -f ${war_file} ${TOMCAT_HOME}/webapps/CloudMonitor5G.war
    rm -rf ${war_file}  
else
    echo "War file not exists, restart the server."
fi

cd ${TOMCAT_HOME}/bin
chmod 777 *.sh

set +x

/bin/bash ${TOMCAT_HOME}/bin/startup.sh

