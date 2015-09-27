(ns record-collection.db.query
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]))

(defn- match-artist-attrs
  [{id :db/id name :artist/name bio :artist/bio sortName :artist/sortName}]
  {:id id 
   :name name 
   :bio (if (empty? bio) "" bio) 
   :sortName (if (empty? sortName) name sortName) 
   })

(defn- match-album-attrs
  [{id :db/id title :album/title year :album/year artists :album/artists albumCover :album/cover}]
    {:id id 
     :title title 
     :year year 
     :artists (set (map :db/id artists))
     :album-cover-id (if (empty? albumCover) nil (:db/id albumCover))})

(defn get-artists []
  (let [artists (d/q '[:find  [(pull ?aid [:db/id :artist/name :artist/bio :artist/sortName]) ...]                              ;?aid ?artist-name ?bio
                       :where [?aid :artist/name _]]
                     (d/db conn))]
    (map match-artist-attrs artists)))

(defn get-artist
  [artist-name]
  (let [artist (d/q '[:find [(pull ?aid [:db/id :artist/name :artist/bio :artist/sortName])]
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

(defn get-album [album-title]
  (let [album (d/q '[:find [(pull ?aid [*])]
                     :in $ ?title
                       :where [?aid :album/title ?title]]
                     (d/db conn)
                     album-title)]
    (match-album-attrs (first album))))

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

(defn get-cover-id [album-id]
  (d/q '[:find ?cover-id .
         :in $ ?aid
         :where [?aid :album/cover ?cover-id]]
       (d/db conn)
       album-id))

(defn get-image [image-id]
  (d/q '[:find ?image .
         :in $ ?iid
         :where [?iid :image/bytes ?image]]
       (d/db conn)
       image-id))