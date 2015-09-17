(ns record-collection.db.query
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]))

(defn get-artists []
  (let [artists (d/q '[:find ?aid ?artist-name
                       :where [?aid :artist/name ?artist-name]]
                     (d/db conn))]
    (map (fn [row] (let [[id artist] row] {:id id :name artist})) artists)))

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
  (map (fn [[a,b]] {:id a :name b}) (d/q '[:find ?aid ?album-title
                                           :in $ ?artist-name
                                           :where [?eid :artist/name ?artist-name]
                                           [?aid :album/artists ?eid]
                                           [?aid :album/title ?album-title]]
                                         (d/db conn)
                                         artist-name)))