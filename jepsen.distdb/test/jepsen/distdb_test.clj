(ns jepsen.distdb-test
  (:require [clojure.test :refer :all]
            [jepsen.core :as jepsen]
            [jepsen.distdb :refer :all]))


(deftest distdb-test
  (is (:valid?
        (:results (jepsen/run! (jepsen.distdb/distsb-test))))))
