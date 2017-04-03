(ns acl2s.core
  (:gen-class)
  (:require [clojure.pprint :refer [pprint]]
            [clojure.test :refer [is]]))
;; Macros to support placing definitions
;; proved to be correct in ACL2S begginer mode into a clojure program

;; ACL2SCode is one of
;; -- Symbol
;; -- Number
;; -- |List ACL2SCode|

;; Any -> Boolean
;; returns if l is a list
(defn listp [l]
  (or (list? l) (nil? l)))

;; ----------------------------------------------------------------------------
;; Any -> Boolean
;; returns if l is a cons
(defn consp [l]
  (and (listp l) (not (empty? l))))

;; ----------------------------------------------------------------------------
;; Any -> Boolean
;; returns if l is an empty list
(defn endp [l]
  (or (= l nil) (and (listp l) (empty? l))))

;; ----------------------------------------------------------------------------
;; |List x| -> |List x|
;; reverses the given list
(defn rev [l]
  (reverse l))

;; ----------------------------------------------------------------------------
;; |List x|, |List y| -> |List |Union x y||
;; Appends list a onto list b
(defn app [a b]
  (concat a b))

;; ----------------------------------------------------------------------------
;; Any, Any -> Boolean
;; returns if two things are equal
(defn equal [a b]
  (= a b))

;; ----------------------------------------------------------------------------
;; Any -> Boolean
;; returns if i is an integer
(defn integerp [i]
  (integer? i))

;; ----------------------------------------------------------------------------
;; Any -> Boolean
;; returns if n is a natural number
(defn natp [n]
  (and (integerp n) (>= n 0)))

;; ----------------------------------------------------------------------------
;; Any -> boolean
;; returns if n is a positive natural number
(defn posp [n]
  (and (pos? n) (integerp n)))

;; ----------------------------------------------------------------------------
;; Any -> Boolean
;; returns if b is a boolean
(defn booleanp [b]
  (or (nil? b) (= b true) (= b false)))

;; ----------------------------------------------------------------------------
;; Boolean, Boolean -> Boolean
;; gives logical a => b
(defmacro implies [a b]
  (if a b true))

(defonce acl2s-substitutions
  (list
    (list 'listp `listp)
    (list 'consp `consp)
    (list 'endp `endp)
    (list 'rev `rev)
    (list 'app `app)
    (list 'equal `equal)
    (list 'integerp `integerp)
    (list 'natp `natp)
    (list 'posp `posp)
    (list 'booleanp `booleanp)
    (list 'implies `implies)
    (list 't true)
    ))

;; ----------------------------------------------------------------------------
;; Symbol, |List |List Symbol |Union Symbol Function||| -> Symbol
;; Takes in a symbol and a list of possible replacements
;; for that symbol. If that symbol is in the replacements
;; list, returns the replacement, else returns the symbol
(defn replace-symb [symb subs]
  (if (empty? subs)
      symb
      (if (= (first (first subs)) symb)
          (second (first subs))
          (recur symb (rest subs)))))

;; ----------------------------------------------------------------------------
;; ACL2SCode -> ACL2SCode
;; Does a first pass over the code body of acl2s code, replacing any symbols
;; with their clojure substituiton. If any exotic (unexpected) data is
;; encountered, it is left in intact
(defn make-replacements [code-body]
  (cond (symbol? code-body)
         (replace-symb code-body acl2s-substitutions)
        (list? code-body)
          (map make-replacements code-body)
         :else code-body))

(defn convert-cond [code-body] code-body)
(defn convert-let [code-body] code-body)

(defn fix-functions [code-body]
  (convert-cond (convert-let code-body)))

(defmacro defunc [function-name arguments input-contract
                  input-test output-contract output-test body]
  (list 'defn function-name (into [] arguments)
        (fix-functions (make-replacements body))))

(defmacro test? [expr] nil)
(defmacro defthm [&stuff] nil)
(defmacro thm [&stuff] nil)
(defmacro defdata [name definition] nil)
(defmacro check= [a b] nil)

(defunc fib (n)
  :input-contract (natp n)
  :output-contract (natp (fib n))
  (if (or (equal n 0) (equal n 1))
      n
      (+ (fib (- n 1))
         (fib (- n 2)))))

(defunc fib-t (a b n)
  :input-contract (and (natp a) (natp b) (natp n))
  :output-contract (natp (fib-t a b n))
  (if (equal n 0)
      b
      (fib-t (+ a b) a (- n 1))))

(defunc fib* (n)
  :input-contract (natp n)
  :output-contract (natp (fib* n))
  (fib-t 1 0 n))

(defn -main
  [& args]
  (println (fib 10))
  (println (fib 20))
  (println (fib 30))
  (println (fib 40))
  (println (fib* 50)))
