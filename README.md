tinystruct2.0
=========
[![Build Status](https://travis-ci.org/m0ver/tinystruct2.0.svg?branch=master)](https://travis-ci.org/m0ver/tinystruct2.0)

This is an example project based on tinystruct2.0, it supports both C/S application and B/S web application development. 

To execute it in CLI mode
---
```tcsh
$ bin/dispatcher --version

  _/  '         _ _/  _     _ _/
  /  /  /) (/ _)  /  /  (/ (  /  2.0
           /
```
```tcsh
$ bin/dispatcher --help
Usage:	dispatcher [--attributes] [actions[/args...]...]
	where attributes include any custom attributes those defined in context 
	or keypair parameters are going to be passed by context,
 	such as: 
	--http.proxyHost=127.0.0.1 or --http.proxyPort=3128 or --param=value
	
$ bin/dispatcher say/"Praise to the Lord"
Praise to the Lord
```

Running it in a servlet container
---
```tcsh
$ bin/dispatcher --start-server --import-applications=org.tinystruct.system.TomcatServer
```

You can access the below urls after deployed this code in Tomcat 6.0+ :

* <a href="http://localhost:8080/?q=say/Praise%20to%20the%20Lord!">http://localhost:8080/?q=say/Praise%20to%20the%20Lord! </a><br />
* <a href="http://localhost:8080/?q=praise">http://localhost:8080/?q=praise </a><br />
* <a href="http://localhost:8080/?q=say/Hello%20World">http://localhost:8080/?q=say/Hello%20World </a><br />
* <a href="http://localhost:8080/?q=youhappy">http://localhost:8080/?q=youhappy</a><br />
* <a href="http://localhost:8080/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F">http://localhost:8080/?q=say/%E4%BD%A0%E7%9F%A5%E9%81%93%E5%85%A8%E4%B8%96%E7%95%8C%E6%9C%80%E7%95%85%E9%94%80%E7%9A%84%E4%B9%A6%E6%98%AF%E5%93%AA%E4%B8%80%E6%9C%AC%E4%B9%A6%E5%90%97%EF%BC%9F</a>
* <a href="http://localhost:8080/?q=tinyeditor">http://localhost:8080/?q=tinyeditor</a><br />

Open the below two urls in different browsers to see the demo:
* <a href="http://localhost:8080/?q=talk/start/Abraham">http://localhost:8080/?q=talk/start/Abraham</a><br />
* <a href="http://localhost:8080/?q=talk/start/Sarah">http://localhost:8080/?q=talk/start/Sarah</a><br />

You will see them in your browser.

<blockquote>
<h1>Praise to the Lord!</h1>
Praise to the Lord! 
<h1>Hello World</h1>
<i>true</i>
<h1>你知道全世界最畅销的书是哪一本书吗？</h1>
</blockquote>

Please check the example in hello.java to get how does the template to be rendered?

```javascript
<javascript>

for(var i=1;i<10;i++) {
	println((i>5?Array(i).join('  '): Array(10 - i).join('  ')) + String.fromCharCode(96+i));
}
</javascript>
```

Result:
--
                a
              b
            c
          d
        e
          f
            g
              h
                i

Demo site: http://smalltalk.tinystruct.org

Also please see this project: 
https://github.com/m0ver/mobile1.0
http://ingod.asia

