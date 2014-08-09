(ns learning-midje.facts
  (:require [midje.sweet :refer :all]
;            [midje.test-util :refer :all]  ;; TODO: fix namespace problem
            [midje.config :as config]))




;;; Facts


;;; from midje guide [facts](https://github.com/marick/Midje/wiki/Facts-v2)
;;; and [examples](https://github.com/marick/Midje/blob/1.6/test/as_documentation/facts.clj

;;  Facts are the smallest for of midje checks
(fact (+ 1 1) => 2)

;; Fact with a docsting

(fact "3 + 4 is 7" (+ 3 4) => 7)

;; a more complicated example
(fact "addition has a unit eliment, 0, such that (+ x 0) => x"
      (+ 0 0) => 0
      (+ 1 0) => 1
      (+ -1 0) => -1
      (+ 1000000 0) => 1000000)

;; the doc string cannot be veriifed but the four checkables can provide some evidence

(fact "addition works in Clojure"
      (+ 10 10) => 20
      (+ 20 20) => 40)

(facts "'Facts' is synonymous with 'fact', I doesn't require multiple checkables"
       (+ 1 1) => 2)

;;; TODO:  fix namepace problem
;(silent-fact "Checkables fail individually."
;             (+ 1 1) => 2
;             (+ 2 2) => 3)
;(note-that (fails 1 time), (fact-expect 2), (fact-actual 4))


;; Nesting facts:

(fact 
   (let [expected 2]
     (+ 1 1) => expected   ; lexically  scoped
     (* 2 1) => expected
     (- 3 1) => expected))


;; facts can be nested
(facts "about arithmetic"
       (fact "there is addition"
             (+ 1 1) => 2)
       (fact "about subraction"
             (- 1 1) => 0)
       (fact "numbers have signs"
             1 => pos?
             -1 => neg?))

;; facts is just a synonym of fact

(comment
  (facts "about arithetic"
         (fact "twice two is three"
               (+ 2 2) => 3))
;  FAIL "about arithetic - twice two is three" at (facts.clj:69)
;    Expected: 3
;      Actual: 4
;      FAILURE: 1 check failed.  (But 16 succeeded.)
)


;;; Tabular facts

;; Facts can contain tables.
(tabular "ways to arrive ar 2"
         (fact
          (?op ?left ?right) => 2)
         ?op ?left ?right
         +    1     1
         *    1     2
         /    4     2)

;; similar to
(fact "ways to arrive at 2"
  (fact
    (+ 1 1) => 2)
  (fact
    (* 1 2) => 2)
  (fact
    (/ 4 2) => 2))



;;; Fact metadata

;; (fact "check Takeuchi's number" :slow ...)
;; the test run tools can skip tests maked :slow



;;; Future facts
;; label a fact as a "todo"

; (future-fact (time np) => polynomial?)

;(capturing-fact-output
; (future-fact "do something someday")
; (fact @fact-output => #"WORK TO DO.*do something someday"))
