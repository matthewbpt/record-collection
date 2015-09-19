(ns record-collection.db.core
  (:require [datomic.api :as d]))

(def uri "datomic:mem://record-collection-test-db")

(d/create-database uri)

(def conn (d/connect uri))

(d/transact conn (load-file "resources/datomic/schema.edn"))

(d/transact conn (load-file "resources/datomic/sample_data.edn"))