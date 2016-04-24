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

;; expect adding three artists to yield three artists in the database
(expect #{ "John" "Paul" "George"}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist {:name "John"})
            (add-artist {:name "Paul"})
            (add-artist {:name "George"})
            (set (map :name (get-artists))))))

;; expect getting artist from id to yield correct artist
(expect "John"
            (with-redefs [conn (create-empty-in-memory-db)]
              (do
                (add-artist {:name "John"})
                (let [id (get-artist-id "John")]
                  (get-artist-name-from-id id)))))

;; get albums for an artist should yield only that artist's albums
(expect #{"Slowhand" "461 Ocean Boulevard"}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist {:name "Eric Clapton"})
            (add-artist {:name "John"})
            (add-album {:title "Slowhand" :artists #{(get-artist-id "Eric Clapton")}})
            (add-album {:title "461 Ocean Boulevard" :artists #{(get-artist-id "Eric Clapton")}})
            (add-album {:title "Test" :artists #{(get-artist-id "John")}})
            (set (map :title (get-albums "Eric Clapton"))))))

(expect #{}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist {:name "Eric Clapton"})
            (delete-entity (get-artist-id "Eric Clapton"))
            (set (map :title (get-artists))))))

;; artist is unique, so adding same artist multiple times should
;; only result in one artist of that name existing
(expect #{"John"}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-artist {:name "John"})
            (add-artist {:name "John"})
            (set (map :name (get-artists))))))

;; test get-artist function as well as martch-artists-attrs
(expect {:name "John" :bio "once upon a time"}
        (in (with-redefs [conn (create-empty-in-memory-db)]
              (do
                (add-artist {:name "John" :bio "once upon a time"})
                (get-artist "John")))))