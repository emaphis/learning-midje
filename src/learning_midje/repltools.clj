(ns learning-midje.repltools
  (:require [midje.sweet :refer :all]
            [midje.config :as config]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; repl tools
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


"
Facts are loaded into a database called the 'compendium'.

They are checked as they are loaded. The last 'fact' loaded is
given specal consideration.

Repl tools alow you to work with the fact 'compendium' and 'autotesting'

"

;;; cloading facts into memory and working with them

(fact 3 => 3)
(fact "3+4 = 7" (+ 3 4) => 7)
 
(comment
  (use 'midje.repl)
  (load-facts)
  (load-facts :all)
  (load-facts 'proj.namespace 'learning-midje.repltools)
  (load-facts 'learning-midje.test.*)  ;; load a namespace tree  
  )

;; :all argument reloads everything in the 'compendium.
;; When a namespace is given those facts in that namespace are reloaded.

;; other features:

(comment
  (load-facts 'namespace :print-facts)  ;; Adjust print verbosity while loading

  ;; Load facts works with metadata
  (load-facts :integration)  ;; Load only integration tests
  (load-facts (complement :integration)) ;; Load non-integration tests
  (load-facts "cute")     ;; Load only facts with "cute" in their names (doc strings)
  (load-facts #"cute[0-6]") ;; Load only facts whose names match the regular expression
  (load-facts some-arbitrary-function-over-a-metadata-map)
  )


;; check facts

(comment
  (check-facts)       ; check same facts over again
  (check-facts *ns* 'learning-midje.repltools) ; check a subset
  (check-facts :all)  ; re-check everything
  )

;; forget facts

(comment
  (forget-facts :all)                 ;; Forget everything
  (forget-facts *ns* 'some.namespace) ;; Forget by namespace
  (forget-facts)                      ;; Forget the current "working set".
  )

;; fetch-facts - return a sequence of some or all of the loaded facts.

(comment
  (fetch-facts :all)                  ;; Return all the facts in the compendium
  (fetch-facts)                       ;; Fetch the current working set.
  (fetch-facts *ns* 'some-namespace)  ;; Fetch by namespace
  )


;; check-one-fact: Check a fact returned from fetch-facts.
(comment 
  (map check-one-fact (fetch-facts))
  )



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; autotesting
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment
  (autotest)
  (autotest :dirs "test/midje/util" "src/midje/util")
  (autotest :filter :core) ; Check only core facts.

  (autotest :pause)
  (autotest :resume)
  (autotest :stop)
  )


;;; the most recentl checked fact

(comment

    (recheck-fact):               ; Check the last fact checked again.
     (last-fact-checked)          ; Returns previous fact as a value.
    (source-of-last-fact-checked) ; Show source of previous fact.

    )
