all:twitter heap

heap: 
	g++ -g -Wall -std=c++11 heap.h -o heap

twitter: twitter.cpp user.o tweet.o datetime.o cmdhandler.o handler.o util.o twiteng.o
	g++ -g -Wall -std=c++11 twitter.cpp user.o tweet.o datetime.o cmdhandler.o handler.o util.o twiteng.o -o twitter

hsort.o:hsort.h
	g++ -g -c -Wall -std=c++11 hsort.h -o hsort.o

user.o: user.cpp user.h hsort.o
	g++ -g -c -Wall -std=c++11 user.cpp -o user.o

tweet.o: tweet.cpp tweet.h
	g++ -g -c -Wall -std=c++11 tweet.cpp -o tweet.o

datetime.o: datetime.cpp datetime.h
	g++ -g -c -Wall -std=c++11 datetime.cpp -o datetime.o

cmdhandler.o: cmdhandler.cpp cmdhandler.h
	g++ -g -c -Wall -std=c++11 cmdhandler.cpp -o cmdhandler.o

handler.o: handler.cpp handler.h
	g++ -g -c -Wall -std=c++11 handler.cpp -o handler.o

util.o: util.cpp util.h
	g++ -g -c -Wall -std=c++11 util.cpp -o util.o

twiteng.o: twiteng.cpp twiteng.h
	g++ -g -c -Wall -std=c++11 twiteng.cpp -o twiteng.o 

clean:
	rm twitter
	rm user.o
	rm util.o
	rm cmdhandler.o
	rm handler.o
	rm datetime.o
	rm twiteng.o
	rm tweet.o
	rm heap
