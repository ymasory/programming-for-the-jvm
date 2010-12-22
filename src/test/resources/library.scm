;; Functions from section 6.3
; Returns the sublist of list obtained by omitting the first k elements.
(define (list-tail x k)
  (if (zero? k)
      x
      (list-tail (cdr x) (- k 1))))

; Return the kth element of list
(define (list-ref list k)
  (car (list-tail list k)))

; These procedures return the first sublist of list whose car is obj,
; where the sublists of list are the non-empty lists return by
; (list-tail list k) for k less than the length of list. If obj does
; not occur in list, then #f (not the empty list) is returned. Memq
; use eq? to compare obj with the elements of list, while memv uses
; eqv? and member uses equal?

(define memq (lambda (obj list)
  (if (eq? list '()) 
      #f
      (if (eq? obj (car list)) 
	  list
	  (memq obj (cdr list))))))
(define memv (lambda (obj list)
  (if (eq? list '()) 
      #f
      (if (eqv? obj (car list)) 
	  list
	  (memv obj (cdr list))))))
(define member (lambda (obj list)
  (if (eq? list '()) 
      #f
      (if (equal? obj (car list)) 
	  list
	  (member obj (cdr list))))))


; Alist (for "association list" must be a list of pairs.  These
; procedures find the first pair in alist whose car field is ob, and
; returns the pair.  if no pair in alist has obj as its car, then #f
; (not the empty list) is returned.  Assq uses eq? to compare obj with
; the car fields of the pairs in alist, while assv uses eqv? and assoc
; uses equal?.
(define (assq obj alist)
  (if (eq? alist '()) 
      #f
      (if (eq? obj (caar alist)) 
	  (car alist)
	  (assq obj (cdr alist)))))
(define (assv obj alist)
  (if (eq? alist '()) 
      #f
      (if (eqv? obj (caar alist)) 
	  (car alist)
	  (assv obj (cdr alist)))))
(define (assoc obj alist)
  (if (eq? alist '()) 
      #f
      (if (equal? obj (caar alist)) 
	  (car alist)
	  (assoc obj (cdr alist)))))

  
