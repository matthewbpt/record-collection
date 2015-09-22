(ns record-collection.db.query
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]))

(defn- match-artist-attrs
  [{id :db/id name :artist/name bio :artist/bio}]
    {:id id :name name :bio bio})

(defn- match-album-attrs
  [{id :db/id title :album/title year :album/year artists :album/artists}]
    {:id id :title title :year year :artists (set (map :db/id artists))})

(defn get-artists []
  (let [artists (d/q '[:find  [(pull ?aid [:db/id :artist/name :artist/bio]) ...]                              ;?aid ?artist-name ?bio
                       :where [?aid :artist/name _]]
                     (d/db conn))]
    (map match-artist-attrs artists)))

(defn get-artist
  [artist-name]
  (let [artist (d/q '[:find [(pull ?aid [:db/id :artist/name :artist/bio])]
                      :in $ ?artist-name
                      :where [?aid :artist/name ?artist-name]]
                    (d/db conn)
                    artist-name)]
    (match-artist-attrs (first artist))))


(defn get-artist-id [artist-name]
  (d/q '[:find ?aid .
         :in $ ?artist-name
         :where [?aid :artist/name ?artist-name]]
       (d/db conn)
       artist-name))

(defn get-artist-name-from-id [id]
  (d/q '[:find ?artist-name .
         :in $ ?aid
         :where [?aid :artist/name ?artist-name]]
       (d/db conn)
       id))

(defn get-albums
  ([]
   (let [albums (d/q '[:find [(pull ?aid [*]) ...]
                       :where [?aid :album/artists _]]
                     (d/db conn))]
     (map match-album-attrs albums)))
  ([artist-name]
   (let [albums (d/q '[:find [(pull ?aid [*]) ...]                                ;?aid ?album-title ?year
                       :in $ ?artist-name
                       :where [?eid :artist/name ?artist-name]
                              [?aid :album/artists ?eid]]
                     (d/db conn)
                     artist-name)]
     (map match-album-attrs albums))))