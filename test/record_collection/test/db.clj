(ns record-collection.test.db
  (:require [expectations :refer :all]
            [record-collection.db.core :refer :all]
            [record-collection.db.transact :refer :all]
            [record-collection.db.query :refer :all]
            [datomic.api :as d]))

(defn create-empty-in-memory-db []
  (let [uri "datomic:mem://record-collection-test-db"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema)
      conn)))

(expect #{["John"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist "John")
            (get-artists))))

(expect "John"
            (with-redefs [conn (create-empty-in-memory-db)]
              (do
                (add-artist "John")
                (let [id (get-artist-id "John")]
                  (get-artist-name-from-id id)))))

(expect #{["Slowhand"] ["461 Ocean Boulevard" ]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist "Eric Clapton")
            (add-artist "John")
            (add-album "Slowhand" "Eric Clapton")
            (add-album "461 Ocean Boulevard" "Eric Clapton")
            (add-album "Test" "John")
            (get-albums "Eric Clapton"))))