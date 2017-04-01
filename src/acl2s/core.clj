(ns acl2s.core
  (:gen-class))
;; Macros to support placing definitions
;; proved to be correct in ACL2S begginer mode into a clojure program

;; ACL2SCode is one of
;; -- Symbol
;; -- Number
;; -- |List ACL2SCode|

(defonce acl2s-substitutions
  '((listp list?)
    (consp cons?)
    (endp empty?)
    (rev reverse)
    (app concat)
    (equal =)
    (integerp integer?)
    (natp (fn [n] (or (= n 0)
                      (and (pos? n) (integer? n)))))
    (posp (fn [n] (and (pos? n) (integer? n))))
    (integerp integer?)
    (t true)
    ))

;; ----------------------------------------------------------------------------
;; Symbol, |List |List Symbol Symbol|| -> Symbol
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

(defmacro defunc [function-name arguments input-contract
                  input-test output-contract output-test body]
  (list 'defn function-name (into [] arguments)
        (list 'if (make-replacements input-test)
                  (make-replacements body)
                  '(throw (Exception. "Input contract failed")))))

(defunc fib (n)
  :input-contract (natp n)
  :output-contract (natp (fib n))
  (if (equal n 0)
      0
      (if (equal n 1)
          1
          (+ (fib (- n 1))
             (fib (- n 2))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (macroexpand '(a 1 2)))
  (println (fib 10)))
