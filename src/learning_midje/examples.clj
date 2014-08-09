(ns learning-midje.examples
  (:require [midje.sweet :refer :all]))


;; facts on 'facts'

(fact 3 => 3)

(fact "description" 3 => 3)

(defn add3
  "add 3 to a given num"
  [num]
  (+ 3 num))

(fact "test add3"
      (add3 4) => 7)

(facts "Testing add3: "
       (fact "(add3 0) should produce 3"       (add3 0) => 3)
       (fact "(add3 -3) should produce 0"      (add3 -3) => 0)
       (fact "(add3 1000) should produce 1003" (add3 1000) => 1003))

