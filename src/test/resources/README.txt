To run the Prolog test:
ensure that . is in the CLASSPATH
> java COM.sootNsmoke.prolog.PrologCompiler test.P
> java COM.sootNsmoke.prolog.Prolog
?- greater(5,3)          
===> true

?- s(1,2)                

===> true
===> No (more) answers

?- greater(2,4)          
===> false
===> No (more) answers

?- s(2,X)
===> X = 3
===> No (more) answers

?= s(X,4)
===> X = 3
===> No (more) answers

Use control-C to quit.

************************************************************************

To run the Scheme test:
> java COM.sootNsmoke.scheme.ReadEvalPrint
> (load "test.scm")
[ a whole bunch of output ]
> (quit)

There are two expected stack dumps which are testing illegal commands.
On my system, the output is:

()
()
()
()
()
()
()
()
()
<compiled procedure lambda34@56f3222d>
8
()
3
()
10
(3 4 5 6)
(5 6)
yes
no
1
()
3
()
5
composite
()
consonant
()
less
greater
equals
#t
#f
(f g)
#t
#t
#t
#f
(b c)
6
35
70
#t
()
6
4 plus 1 equals 
5
()
#(0 1 2 3 4)
25
(list 3 4)
(list a 'a)
(a 3 4 5 6 b)
((foo 7) . cons)
#(10 5 2 4 3 8)
`(a)
(a `(b ,(+ 1 2) ,(foo 4 d) e) f)
()
6
()
1
45
#t
#f
#f
#f
#f
#f
#t
#f
#f
#f
#t
#f
#f
#t
#f
#t
#t
#t
#f
#f
#f
#t
#t
#f
#f
#t
#t
#t
#f
#f
#t
#t
#t
#t
#t
#t
#t
#t
#t
#t
#t
#f
()
()
(a b c)
#t
()
(a . 4)
#t
(a . 4)
#f
()
#f
#t
#t
#f
#f
(a)
((a) b c d)
("a" b c)
(a . 3)
((a b) . c)
a
(a)
1
Error:
Stacktrace:
java.lang.NullPointerException: 
	at COM.sootNsmoke.scheme.car.apply1(SchemeLibrary.java:467)
	at sym127.apply0(Unknown Source)
	at COM.sootNsmoke.scheme.ReadEvalPrint.eval(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.run(Compiled Code)
	at COM.sootNsmoke.scheme.load.apply1(SchemeLibrary.java:1589)
	at sym1.apply0(Unknown Source)
	at COM.sootNsmoke.scheme.ReadEvalPrint.eval(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.run(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.main(Compiled Code)
Stacktrace done
()
(b c d)
2
Error:
Stacktrace:
java.lang.NullPointerException: 
	at COM.sootNsmoke.scheme.cdr.apply1(SchemeLibrary.java:472)
	at sym130.apply0(Unknown Source)
	at COM.sootNsmoke.scheme.ReadEvalPrint.eval(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.run(Compiled Code)
	at COM.sootNsmoke.scheme.load.apply1(SchemeLibrary.java:1589)
	at sym1.apply0(Unknown Source)
	at COM.sootNsmoke.scheme.ReadEvalPrint.eval(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.run(Compiled Code)
	at COM.sootNsmoke.scheme.ReadEvalPrint.main(Compiled Code)
Stacktrace done
()
()
()
()
()
#t
#t
#f
#f
(a 7 c)
()
3
3
0
(x y)
(a b c d)
(a (b) (c))
(a b c . d)
a
(c b a)
((e (f)) d (b c) a)
(a b c)
(b c)
#f
#f
((a) c)
#f
(101 102)
()
(a 1)
(b 2)
#f
#f
((a))
#f
(5 7)
#t
#t
#f
#t
#f
#f
"flying-fish"
"martin"
"Malvina"
#t
mISSISSIppi
#f
#t
#t
#t
#f
#t
#t
#t
#f
#t
3
#\b
(#\a #\b #\c)
"abc"
#(a b c)
8
13
#(0 ("Sue" "Sue") "Anna")
"doe"
(dah dah didah)
#(dididit dah)
7
()
30
(b e h)
(1.0d0 4.0d0 27.0d0 256.0d0 3125.0d0)
(5 7 9)
(1 2 3)
3
(3 3)
#f
#t
#f
(5 6)
()
> 

