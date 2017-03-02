import platform
import psutil
import sys
import json
import urllib2
import os
import datetime


os.environ['no_proxy'] = '127.0.0.1,localhost'
#ENTER YOUR USERNAME HERE
uname = 'seven'
flag = False

ip = '162.243.66.62:6969'
#ip = '127.0.0.1:6969'

def bytes_norm(n):
    symbols = ('K', 'M', 'G', 'T')
    prefix = {}
    for i, s in enumerate(symbols):
        prefix[s] = 1 << (i + 1) * 10

    for i in reversed(symbols):
        if (n >= prefix[i]):
            value = float(n) / prefix[i]
            return '%.2f' % (value)
    return '%sB' % n


def clientInfo():
        flag = True
        print("Client")
        os={'uname':uname,'node': platform.node(),
        'system':platform.system(),
        'release':platform.release()}
        req = urllib2.Request('http://'+ip+'/clientInfoInsert')
        req.add_header('Content-Type', 'application/json')
        urllib2.urlopen(req,json.dumps(os))
        sysInfo()

def Main():
    if flag == False:
        clientInfo()
    else:
        sysInfo()

def sysInfo():

    sys_info_arr = []

    print("SYS INFO")
    for part in psutil.disk_partitions(all=False):
        if part.fstype == '':
            continue

        y = ""
        y = ''.join(part.device)

        sys_info = {
                    'Total': bytes_norm(psutil.disk_usage(part.mountpoint).total),
                    'Used': bytes_norm(psutil.disk_usage(part.mountpoint).used),
                    'Free': bytes_norm(psutil.disk_usage(part.mountpoint).free),
                    'Percent': psutil.disk_usage(part.mountpoint).percent,
                    'Device': y,
                    }

        sys_info_arr.append(sys_info)  # sys_info_arr stores memory details of each physical drive


    print(json.dumps(sys_info_arr))
    jsonData = {}
    jsonData['uname'] = uname
    jsonData['CPU Percent'] = psutil.cpu_percent()
    jsonData['System Stats'] =  sys_info_arr
    req2 = urllib2.Request('http://'+ip+'/clientInsertServerStats')
    req2.add_header('Content-Type', 'application/json')
    response = urllib2.urlopen(req2,json.dumps(jsonData))
    networkInfo()

def networkInfo():
    network_arr=[]
    temp=[]

    temp=psutil.net_io_counters(pernic=True)

    for nic in psutil.net_io_counters(pernic=True):
        x={
           'NIC':nic,
           'Bytes sent': temp[nic].bytes_sent,
           'Bytes received':temp[nic].bytes_recv,
           'Packets Sent':temp[nic].packets_sent,
           'Packet received':temp[nic].packets_recv
        }
        network_arr.append(x)
    data = {}
    data['uname'] = uname
    data['Network data'] = network_arr
    data['time'] =  str(datetime.datetime.now())
    req2 = urllib2.Request('http://'+ip+'/networkInfoInsert')
    req2.add_header('Content-Type', 'application/json')
    response = urllib2.urlopen(req2,json.dumps(data))


#main init
if __name__ == "__main__":
    Main()
