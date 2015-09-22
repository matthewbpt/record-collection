(ns record-collection.db.transact
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]
            [record-collection.db.query :refer :all]))

(defn add-artist [artist]
  @(d/transact conn (vec (filter (complement nil?)
                                 [{:db/id       #db/id[:db.part/user -100001]
                                   :artist/name (:name artist)}
                                  (if (not-empty (:bio artist))
                                    {:db/id      #db/id[:db.part/user -100001]
                                     :artist/bio (:bio artist)})]))))

(defn add-album [album-title artist-name]
  @(d/transact conn [{:db/id         #db/id[:db.part/user -100001]
                      :album/title   album-title
                      :album/artists (get-artist-id artist-name)}]))
