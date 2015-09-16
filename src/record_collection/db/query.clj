(ns record-collection.db.query
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]))

(defn get-artists []
  (d/q '[:find ?artist-name
         :where [?aid :artist/name ?artist-name]]
       (d/db conn)))

(defn get-artist-id [artist-name]
  (ffirst (d/q '[:find ?aid
                 :in $ ?artist-name
                 :where [?aid :artist/name ?artist-name]]
               (d/db conn)
               artist-name)))

(defn get-artist-name-from-id [id]
  (ffirst (d/q '[:find ?artist-name
                 :in $ ?aid
                 :where [?aid :artist/name ?artist-name]]
               (d/db conn)
               id)))

(defn get-albums [artist-name]
  (d/q '[:find ?album-title
         :in $ ?artist-name
         :where [?aid :album/title ?album-title]
                [?aid :album/artists ?eid]
                [?eid :artist/name ?artist-name]]
       (d/db conn)
       artist-name))