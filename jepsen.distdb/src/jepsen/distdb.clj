(ns jepsen.distdb
  (:require [clojure.tools.logging :refer :all :as log]
            [clojure.string :refer :all]
            [clj-http.client :as http]
            [knossos.model :as model]
            [jepsen
             [db :as db]
             [checker :as checker]
             [control :as c]
             [client  :as client]
             [generator :as gen]
             [nemesis :as nemesis]
             [util :refer [timeout]]
             [tests :as tests]]
            [jepsen.os.ubuntu :as ubuntu]))

(defn db
  "Node bootstrap"
  [version]
  (reify db/DB
    (setup! [_ test node]
      (log/info node "setting up distdb" version))
    (teardown! [_ test node]
      (log/info node "tearing down distdb"))
    )
  )

(defn r [_ _] {:type :invoke, :f :read, :value nil})
(defn w [_ _] {:type :invoke, :f :write, :value (rand-int 5)})

(defn client
  "A simple http client"
  [host]
  (reify client/Client
    (setup! [_ test node]
      (client (name node)))
    (invoke! [this test op]
      (timeout 5000 (assoc op
                      :type :info,
                      :error :timeout)
               (case (:f op)
                 :read (assoc op
                         :type :ok,
                         :value (read-string (get (http/get (join ["http://" host ":8000/db"])) :body)))
                 :write (do (http/post "http://n1:8000/db" {:body (str (:value op))})
                            (assoc op :type :ok)))))
    (teardown! [_ test]))
  )

(defn distsb-test
  []
    (assoc tests/noop-test
      :name "distdb"
      :os ubuntu/os
      :db (db "1.0")
      :client (client nil)
      :checker checker/linearizable
      :model (model/register 5)
      :generator (->> (gen/mix [r w])
                      (gen/stagger 0.1)
                      (gen/clients)
                      (gen/time-limit 5))))