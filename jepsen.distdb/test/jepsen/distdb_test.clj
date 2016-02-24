(ns jepsen.distdb-test
  (:require [clojure.test :refer :all]
            [jepsen.core :as jepsen]
            [jepsen.distdb :refer :all]))

(deftest distdb-test
  (is (:valid?
        (:results (jepsen/run! (assoc (jepsen.distdb/distsb-test)
                                 :ssh {
                                       :strict-host-key-checking false
                                       :private-key-path "~/.ssh/grebennikov_roman.pem"
                                       :username "root"}))))))
