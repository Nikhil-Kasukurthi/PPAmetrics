# PPAmetrics
###An easy way to monitor your server. 
![alt tag](https://raw.githubusercontent.com/Nikhil-Kasukurthi/PPAmetrics/master/icon.jpg)

###Initial setup
####For Ubuntu servers 
Firstly, run [this](https://raw.githubusercontent.com/Nikhil-Kasukurthi/PPAmetrics/master/install.sh) shell script on your server. In extracted directory, run it using
```
sh install.sh
```
It will install pip and then psutil package on your server.
####Other distributions
For other distribution, please install pip and psutil packages on your server.

###Linking your server to PPAmetrics
In Memory.py file, update your username to start tracking data from your server after signing up through our app.

```python 
uname = 'Your username'
app_secert = 'Your app secret'
```

Run the python script using the fowllowing command

```sudo nohup python Memory.py&```
