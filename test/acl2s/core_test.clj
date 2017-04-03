(ns acl2s.core-test
  (:require [clojure.test :refer :all]
            [acl2s.core :refer :all]))

(deftest acl2s-std-lib-test
  (testing "listp tests"
           (is (listp nil))
           (is (listp '()))
           (is (listp '(1 2 a b)))
           (is (listp (list 'a 'b 1 2)))
           (is (not (listp 2)))
           (is (not (listp 'a)))
           (is (not (listp [])))
           (is (not (listp [1 2 3])))
           (is (not (listp {})))
           (is (not (listp {:key 'value})))
           (is (not (listp :item)))
           (is (not (listp #{'set 'of 'items}))))
  (testing "consp tests"
           (is (not (consp nil)))
           (is (not (consp '())))
           (is (consp '(1 2 a b)))
           (is (consp (list 'a 'b 1 2)))
           (is (not (consp 2)))
           (is (not (consp 'a)))
           (is (not (consp [])))
           (is (not (consp [1 2 3])))
           (is (not (consp {})))
           (is (not (consp {:key 'value})))
           (is (not (consp :item)))
           (is (not (consp #{'set 'of 'items}))))
  (testing "endp tests"
           (is (endp nil))
           (is (endp '()))
           (is (not (endp '(1 2 a b))))
           (is (not (endp (list 'a 'b 1 2))))
           (is (not (endp 2)))
           (is (not (endp 'a)))
           (is (not (endp [])))
           (is (not (endp [1 2 3])))
           (is (not (endp {})))
           (is (not (endp {:key 'value})))
           (is (not (endp :item)))
           (is (not (endp #{'set 'of 'items})))))

(deftest symbol-replacement-test
  (testing "Symbol replacement"
           (is (= (replace-symb 'a '((a b))) 'b))
           (is (= (replace-symb 'hello '((a b))) 'hello))
           (is (= (replace-symb 'a '()) 'a))
           (is (= (replace-symb 'a '((b a) (a c))) 'c))))

