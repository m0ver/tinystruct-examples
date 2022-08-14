<img src="https://raw.githubusercontent.com/tinystruct/tinystruct2.0/master/favicon.png" title="tinystruct2.0" /> 

tinystruct framework
=========
[![Build Status](https://travis-ci.org/m0ver/tinystruct-examples.svg?branch=master)](https://travis-ci.org/m0ver/tinystruct2.0)

This is an example project based on tinystruct framework, it supports both C/S application and B/S web application development. 



To execute it in CLI mode
---
```tcsh
$ bin/dispatcher --version

  _/  '         _ _/  _     _ _/
  /  /  /) (/ _)  /  /  (/ (  /  0.5.5
           /
```
```tcsh
$ bin/dispatcher --help
Usage: bin/dispatcher COMMAND [OPTIONS]
Commands: 
        account                 
        download                Download a resource from other servers
        error                   
        exec                    To execute native command(s)
        generate                
        image2base64            
        info                    
        install                 Install a package
        login                   
        praise                  
        read                    
        render                  
        say                     
        set                     Set system property
        smile                   
        talk                    
        talk/command            
        talk/join               
        talk/matrix             
        talk/save               
        talk/start              
        talk/testing            
        talk/topic              
        talk/update             
        talk/upload             
        talk/version            
        time                    
        time/start              
        time/stop               
        time/update             
        tinyeditor              
        tinyeditor/save         
        tinyeditor/update       
        tinyeditor/version      
        update                  Update for latest version
        user                    
        users                   
        version                 
        youhappy                

Options: 
        --help                  Help command
        --import                Import application
        --logo                  Print logo
        --settings              Print settings
        --version               Print version

Run 'bin/dispatcher COMMAND --help' for more information on a command.

$ bin/dispatcher say/"Praise to the Lord"
Praise to the Lord
```

Run it in a servlet container
---
```tcsh
# bin/dispatcher start --import org.tinystruct.system.TomcatServer
```
Run it in docker container
---
```tcsh
# wget https://github.com/tinystruct/tinystruct2.0/archive/master.zip
# unzip master.zip
# mv tinystruct2.0-master/Dockerfile .
# docker build -t tinystruct-based:1.0 -f Dockerfile .
# docker run -d -p 777:777 tinystruct-based:1.0
```

You can access the below URLs after deployed the project in Tomcat 6.0+ :

* <a href="http://localhost:777/?q=say/Praise%20to%20the%20Lord!">http://localhost:777/?q=say/Praise%20to%20the%20Lord! </a><br />
* <a href="http://localhost:777/?q=praise">http://localhost:777/?q=praise </a><br />
* <a href="http://localhost:777/?q=youhappy">http://localhost:777/?q=youhappy</a><br />
* <a href="http://localhost:777/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F">http://localhost:777/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F</a>

A demonstration for comet technology, without any websocket and support any web browser:
* <a href="https://tinystruct.herokuapp.com/?q=talk">https://tinystruct.herokuapp.com/?q=talk</a><br />

<img src="example.png" title="smalltalk - tinystruct2.0" height="300"/> <br />

Live Demo Site: 
* https://tinystruct.herokuapp.com/
* <a href="https://tinystruct.herokuapp.com/?q=say/Praise%20to%20the%20Lord!">https://tinystruct.herokuapp.com/?q=say/Praise%20to%20the%20Lord! </a><br />
* <a href="https://tinystruct.herokuapp.com/?q=praise">https://tinystruct.herokuapp.com/?q=praise</a><br />
* <a href="https://tinystruct.herokuapp.com/?q=youhappy">https://tinystruct.herokuapp.com/?q=youhappy</a><br />
* <a href="https://tinystruct.herokuapp.com/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F">https://tinystruct.herokuapp.com/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F</a>

Results in your browser should be:

<blockquote>
<h1>Praise to the Lord!</h1>
Praise to the Lord! 
<i>true</i>
<h1>你知道全世界最畅销的书是哪一本书吗？</h1>
</blockquote>

Explore it 
--
* Please read more example code in the project.
* Also please see this project: 
	https://github.com/m0ver/mobile1.0
	http://ingod.asia


License
--

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/m0ver/tinystruct2.0/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
