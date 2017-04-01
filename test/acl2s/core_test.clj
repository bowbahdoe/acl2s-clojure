(ns acl2s.core-test
  (:require [clojure.test :refer :all]
            [acl2s.core :refer :all]))

(deftest symbol-replacement-test
  (testing "Symbol replacement"
           (is (= (replace-symb 'a '((a b))) 'b))
           (is (= (replace-symb 'hello '((a b))) 'hello))
           (is (= (replace-symb 'a '()) 'a))
           (is (= (replace-symb 'a '((b a) (a c))) 'c))))



(def acl2s-fib
  '(cond ((equal n 0) 0)
          ((equal n 1) 1)
          (t (+ (fib (- n 1))
                (fib (- n 2))))))
(def clojure-fib
  '(cond ((= n 0) 0)
          ((= n 1) 1)
          (true (+ (fib (- n 1))
                (fib (- n 2))))))

(deftest make-replacements-test
  (testing "Making function name replacements"
           (is (= (make-replacements acl2s-fib) clojure-fib))))
