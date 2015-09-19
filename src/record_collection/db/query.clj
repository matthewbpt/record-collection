(ns record-collection.db.query
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]))

(defn- match-artist-attrs
  [{id :db/id name :artist/name bio :artist/bio}]
    {:id id :name name :bio bio})

(defn- match-album-attrs
  [{id :db/id title :album/title year :album/year}]
    {:id id :title title :year year})

(defn get-artists []
  (let [artists (d/q '[:find  [(pull ?aid [:db/id :artist/name :artist/bio]) ...]                              ;?aid ?artist-name ?bio
                       :where [?aid :artist/name _]]
                     (d/db conn))]
    (map match-artist-attrs artists)))

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

(defn get-albums [artist-name]
  (let [albums (d/q '[:find [(pull ?aid [:db/id :album/title :album/year]) ...]                                ;?aid ?album-title ?year
                     :in $ ?artist-name
                     :where [?eid :artist/name ?artist-name]
                            [?aid :album/artists ?eid]]
                   (d/db conn)
                   artist-name)]
    (map match-album-attrs albums)))