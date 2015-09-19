(ns record-collection.db.transact
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]
            [record-collection.db.query :refer :all]))

(defn add-artist [artist-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                     :artist/name artist-name}]))

(defn add-album [album-title artist-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :album/title album-title
                      :album/artists (get-artist-id artist-name)}]))

;(defn add-example-data []
;  (add-artist "Eric Clapton")
;  (add-album "Slowhand" "Eric Clapton")
;  (add-album "461 Ocean Boulevard" "Eric Clapton")
;  (add-artist "John Mayall & the Bluesbreakers")
;  (add-album "Blues Breakers with Eric Clapton" "John Mayall & the Bluesbreakers"))

;(add-example-data)

;(add-artist "Steven Wilson")
;(add-artist "Joni Mitchell")