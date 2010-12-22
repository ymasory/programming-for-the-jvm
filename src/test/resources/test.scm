;; This is a series of tests of the Scheme compiler
;; These are taken directly from the Scheme R4 specification
(load "library.scm")

; Section 4.1.4 Lambda expressions
(lambda (x) (+ x x))        ; ==> a procedure
((lambda (x) (+ x x)) 4)    ; ==> 8
(define reverse-subtract
    (lambda (x y) (- y x)))
(reverse-subtract 7 10)     ; ==> 3

(define add4
    (let ((x 4))
        (lambda (y) (+ x y))))
(add4 6)                    ; ==> 10
((lambda x x) 3 4 5 6)      ; ==> (3 4 5 6)
((lambda (x y . z) z)
    3 4 5 6)                ; ==> (5 6)

; Section 4.1.5 Conditionals
(if (> 3 2) 'yes 'no)       ; ==> yes
(if (> 2 3) 'yes 'no)       ; ==> no
(if (> 3 2)
    (- 3 2)
    (+ 3 2))                ; ==> 1


; section 4.1.6 Assignments
(define x 2)
(+ x 1)                     ; ==> 3
(set! x 4)                  ; ==> unspecified
(+ x 1)                     ; ==> 5


; Section 4.2.1 Conditionals
(case (* 2 3)
    ((2 3 5 7) 'prime)
    ((1 4 6 8 9) 'composite))

(case (car '(c d))
    ((a) 'a)
    ((b) 'b))

(case (car '(c d))
    ((a e i o u) 'vowel)
    ((w y) 'semivowel)
    (else 'consonant))


(define (f a b)
(cond
  ((> a b) 'greater)
  ((< a b) 'less)
  (else 'equals)
  )
  )

(f 1 2)
(f 4 3)
(f 0 0)

(and (= 2 2) (> 2 1))
(and (= 2 2) (< 2 1))
(and 1 2 'c '(f g))
(and)

(or (= 2 2) (> 2 1))  ; ==> #t
(or (= 2 2) (< 2 1))  ; ==> #t
(or #f #f #f)         ; ==> #f
(or (memq 'b '(a b c)) (/ 3 0))
                       ; ==> (b c)

; Section 4.2.2 Binding constructs
(let ((x 2) (y 3))
    (* x y))            ; ==> 6
(let ((x 2) (y 3))
    (let ((x 7)
          (z (+ x y)))
      (* z x)))         ; ==> 35

(let ((x 2) (y 3))
    (let* ((x 7)
           (z (+ x y)))
      (* z x)))         ; ==> 70

(letrec ((even?
          (lambda (n)
            (if (zero? n)
                #t
              (odd? (- n 1)))))
         (odd?
          (lambda (n)
            (if (zero? n)
                #f
              (even? (- n 1))))))
        (even? 88))                    ; ==> #t

; Section 4.2.3 Sequencing
(define x 0)
(begin (set! x 5)
       (+ x 1))             ; ==> 6

(begin (display "4 plus 1 equals ")
       (display (+ 4 1)))   ; ==> unspecified

; Section 4.2.4 Iteration

(do ((vec (make-vector 5))
     (i 0 (+ i 1)))
    ((= i 5) vec)
   (vector-set! vec i i))   ; ==> #(0 1 2 3 4)

(let ((x '(1 3 5 7 9)))
  (do ((x x (cdr x))
       (sum 0 (+ sum (car x))))
      ((null? x) sum)))     ; ==> 25

; Section 4.2.6 Quasiquotation
`(list ,(+ 1 2) 4)                      ; ==> (list 3 4)
(let ((name 'a)) `(list ,name ',name))  ; ==> (list a (quote a))
`(a ,(+ 1 2) ,@(map abs '(4 -5 6)) b)   ; ==> (a 3 4 5 6 b)
`((foo ,(- 10 3)) ,@(cdr '(c)) . ,(car '(cons)))    ; ==> ((foo 7) . cons)
`#(10 5 ,(sqrt 4) ,@(map sqrt '(16 9)) 8)
                                       ; ==> #(10 5 2 4 3 8)
``(a)                                   ; ==> `(a)

`(a `(b ,(+ 1 2) ,(foo ,(+ 1 3) d) e) f)
                                       ; ==> (a `(b ,(+1 2) ,(foo 4 d) e) f)


; Section 5.2.1 Top level definitions
(define add3 (lambda (x) (+ x 3)))
(add3 3)                                ; ==> 6
(define first car)
(first '(1 2))                          ; ==> 1

; Section 5.2.2 Internal definitions
(let ((x 5))
  (define foo (lambda (y) (bar x y)))
  (define bar (lambda (a b) (+ (* a b) a)))
  (foo (+ x 3)))                        ; ==> 45

; Section 6.1 Booleans
#t              ; ==> #t
#f              ; ==> #f
'#f             ; ==> #f
(not #t)        ; ==> #f
(not 3)         ; ==> #f
(not (list 3))  ; ==> #f
(not #f)        ; ==> #t
(not '())       ; ==> #f
(not (list))    ; ==> #f
(not 'nil)      ; ==> #f

(boolean? #f)   ; ==> #t
(boolean? 0)    ; ==> #f
(boolean? '())  ; ==> #f

; Section 6.2 Equivalence predicates
(eqv? 'a 'a)                        ; ==> #t
(eqv? 'a 'b)                        ; ==> #f
(eqv? 2 2)                          ; ==> #t
(eqv? '() '())                      ; ==> #t
(eqv? 100000000 100000000)          ; ==> #t
(eqv? (cons 1 2) (cons 1 2))        ; ==> #f
(eqv? (lambda () 1) (lambda () 2))  ; ==> #f
(eqv? #f 'nil)                      ; ==> #f
(let ((p (lambda (x) x)))
    (eqv? p p))                     ; ==> #t

(eq? 'a 'a)                         ; ==> #t
(eq? '(a) '(a))                     ; ==> unspecified
(eq? (list 'a) (list 'a))           ; ==> #f
(eq? "a" "a")                       ; ==> unspecified
(eq? "" "")                         ; ==> unspecified
(eq? '() '())                       ; ==> #t
(eq? 2 2)                           ; ==> unspecified
(eq? #\A #\A)                       ; ==> unspecified
(eq? car car)                       ; ==> #t
(let ((n (+ 2 3))) (eq? n n))       ; ==> unspecified
(let ((x '(a))) (eq? x x))          ; ==> #t
(let ((x '#())) (eq? x x))          ; ==> #t
(let ((p (lambda (x) x))) (eq? p p)); ==> #t

(equal? 'a 'a)                      ; ==> #t
(equal? '(a) '(a))                  ; ==> #t
(equal? '(a (b) c) '(a (b) c))      ; ==> #t
(equal? "abc" "abc")                ; ==> #t
(equal? 2 2)                        ; ==> #t
(equal? (make-vector 5 'a)
        (make-vector 5 'a))         ; ==> #t
(equal? (lambda (x) x)
        (lambda (y) y))             ; ==> unspecified

; Section 6.3 Pairs and lists
(define x (list 'a 'b 'c))
(define y x)
y                                   ; ==> (a b c)
(list? y)                           ; ==> #t
(set-cdr! x 4)                      ; ==> unspecified
x                                   ; ==> (a . 4)
(eqv? x y)                          ; ==? #t
y                                   ; ==> (a . 4)
(list? y)                           ; ==> #f
(set-cdr! x x)                      ; ==> unspecified
(list? x)                           ; ==> #f

(pair? '(a . b))                    ; ==> #t
(pair? '(a b c))                    ; ==> #t
(pair?  '())                        ; ==> #f
(pair? '#(a b))                     ; ==> #f

(cons 'a '())                       ; ==> (a)
(cons '(a) '(b c d))                ; ==> ((a) b c d)
(cons "a" '(b c))                   ; ==> ("a" b c)
(cons 'a 3)                         ; ==> (a . 3)
(cons '(a b) 'c)                    ; ==> ((a b) . c)

(car '(a b c))                      ; ==> a
(car '((a) b c d))                  ; ==> (a)
(car '(1 . 2))                      ; ==> 1
(car '())                           ; ==> error

(cdr '((a) b c d))                  ; ==> (b c d)
(cdr '(1 . 2))                      ; ==> 2
(cdr '())                           ; ==> error

(define (f) (list 'not-a-constant-list))
(define (g) '(constant-list))
(set-car! (f) 3)                    ; ==> unspecified
(set-car! (g) 3)                    ; ==> error

(list? '(a b c))                    ; ==> #t
(list? '())                         ; ==> #t
(list? '(a . b))                    ; ==> #f
(let ((x (list 'a)))
  (set-cdr! x x)
  (list? x))                        ; ==> #f
(list 'a (+ 3 4) 'c)                ; ==> (a 7 c)
(list)                              ; ==> ()

(length '(a b c))                   ; ==> 3
(length '(a (b) (c d e)))           ; ==> 3
(length '())                        ; ==> 0

(append '(x) '(y))                  ; ==> (x y)
(append '(a) '(b c d))              ; ==> (a b c d)
(append '(a (b)) '((c)))            ; ==> (a (b) (c))

(append '(a b) '(c . d))            ; ==> (a b c . d)
(append '() 'a)                     ; ==> 'a

(reverse '(a b c))                  ; ==> (c b a)
(reverse '(a (b c) d (e (f))))      ; ==> ((e (f)) d (b c) a)

(memq 'a '(a b c))                  ; ==> (a b c)
(memq 'b '(a b c))                  ; ==> (b c)
(memq 'a '(b c d))                  ; ==> #f
(memq (list 'a) '(b (a) c))         ; ==> #f
(member (list 'a) '(b (a) c))       ; ==> ((a) c)
(memq 101 '(100 101 102))           ; ==> unspecified
(memv 101 '(100 101 102))           ; ==> (101 102)

(define e '((a 1) (b 2) (c 3)))
(assq 'a e)                         ; ==> (a 1)
(assq 'b e)                         ; ==> (b 2)
(assq 'd e)                         ; ==> #f
(assq (list 'a) '(((a)) ((b)) ((c))))
                                    ; ==> #f
(assoc (list 'a) '(((a)) ((b)) ((c))))
                                    ; ==> ((a))
(assq 5 '((2 3) (5 7) (11 13)))     ; ==> unspecified
(assv 5 '((2 3) (5 7) (11 13)))     ; ==> (5 7)

; Section 6.4 Symbols
(symbol? 'foo)                      ; ==> #t
(symbol? (car '(a b)))              ; ==> #t
(symbol? "bar")                     ; ==> #f
(symbol? 'nil)                      ; ==> #t
(symbol? '())                       ; ==> #f
(symbol? #f)                        ; ==> #f

(symbol->string 'flying-fish)       ; ==> "flying-fish"
(symbol->string 'Martin)            ; ==> "martin"
(symbol->string
  (string->symbol "Malvina"))       ; ==> "Malvina"

(eq? 'mISSISSIppi 'mississippi)     ; ==> #t
(string->symbol "mISSISSIppi")      ; ==> the symbol with the name "mISSISSIppi"
(eq? 'bitBlt (string->symbol "bitBlt")) ; ==> #f
(eq? 'JollyWog
     (string->symbol
       (symbol->string 'JollyWog))) ; ==> #t
(string=? "K. Harper, M.D."
          (symbol->string
            (string->symbol "K. Harper, M.D.")))
                                    ; ==> #t


; Section 6.7 Strings
(string=? "a" "a")                  ; ==> #t
(string=? "a" "A")                  ; ==> #f
(string-ci=? "a" "A")               ; ==> #t
(string<? "a" "b")                  ; ==> #t
(string<? "book" "bookkeeper")      ; ==> #t
(string>=? "foo" "foobar")          ; ==> #f
(string=? "a" (make-string 1 #\a))  ; ==> #t
(string-length "abc")               ; ==> 3
(string-ref "abc" 1)                ; ==> b
(string->list "abc")                ; ==> (a b c)
(list->string '(#\a #\b #\c))              ; ==> "abc"

; Section 6.8 Vectors
(vector 'a 'b 'c)                   ; ==> #(a b c)
(vector-ref '#(1 1 2 3 4 8 13 21)
   5)                               ; ==> 8
(vector-ref '#(1 1 2 3 4 8 13 21)
            (inexact->exact (round (* 2 (acos -1)))))
                                    ; ==> 13
(let ((vec (vector 0 '(2 2 2 2) "Anna")))
  (vector-set! vec 1 '("Sue" "Sue"))
  vec)                              ; ==> #(0 ("Sue" "Sue") "Anna")
(vector-set! '#(0 1 2) 1 "doe")
                                    ; ==> error
(vector->list '#(dah dah didah))    ; ==> (dah dah didah)
(list->vector '(dididit dah))       ; ==> (dididit dah)


; Section 6.9 Control features
(apply + (list 3 4))                ; ==> 7
(define compose
  (lambda (f g)
    (lambda args
      (f (apply g args)))))
((compose sqrt *) 12 75)            ; ==> 30

(map cadr '((a b) (d e) (g h)))     ; ==> (b e h)
(map (lambda (n) (expt n n))
    '(1 2 3 4 5))                   ; ==> (1 4 27 256 3125)
(map + '(1 2 3) '(4 5 6))           ; ==> (5 7 9)
(let ((count 0))
    (map (lambda (ignored)
           (set! count (+ count 1))
           count)
         '(a b c)))                 ; ==> unspecified

(force (delay (+ 1 2)))      ; ==> 3
(let ((p (delay (+ 1 2))))
    (list (force p) (force p))) ; ==> (3 3)

(> 4/3 2)
(> 3+2i 0)
(< 4/2 2)
((lambda (x y . z) z) 3 4 5 6)


