(ns record-collection.db.transact
  (:require [datomic.api :as d]
            [record-collection.db.core :refer :all]
            [record-collection.db.query :refer :all]
            [clojure.string :as str]))

(defn add-artist [artist]
  @(d/transact conn (vec (filter (complement nil?)
                                 [{:db/id       #db/id[:db.part/user -100001]
                                   :artist/name (:name artist)}
                                  (if (not-empty (:bio artist))
                                    {:db/id      #db/id[:db.part/user -100001]
                                     :artist/bio (:bio artist)})
                                  (if (not-empty (:sortName artist))
                                    {:db/id      #db/id[:db.part/user -100001]
                                     :artist/sortName (:sortName artist)})]))))

(defn add-album [album]
  @(d/transact conn (vec (filter (complement nil?)
                                 [{:db/id         #db/id[:db.part/user -100001]
                      :album/title   (:title album)}
                     (if (not (nil? (:year album)))
                       {:db/id         #db/id[:db.part/user -100001]
                      :album/year   (:year album)})
                      (if (not-empty (:artists album))
                       {:db/id         #db/id[:db.part/user -100001]
                      :album/artists   (:artists album)})]))))

(defn add-album-cover [album-id cover]
  @(d/transact conn [{:db/id #db/id[:db.part/user -100001]
                      :image/bytes (:bytes cover)
                      :image/name (:filename cover)
                      :image/type (:content-type cover)}
                     {:db/id album-id
                      :album/cover #db/id[:db.part/user -100001]}]))

(def artist-vec
  [ "Animals As Leaders" "Bach" "Baron" "Beach Boys, The" "Beatles, The" "Becker and Fagen" "Bowie, David" "Buckley, Jeff" "Cale, JJ" "Clapton, Eric" "Cohen, Leonard" "Cray, Robert" "Crosby, Stills, Nash and Young" "Denver, John" "Dire Straits" "Dixon, Willie" "Drake, NIck" "Ian Dury and the Blockheads" "Dylan, Bob" "Eagles" "Ebony Steel Band" "Emerson Lake and Palmer" "Fagen, Donald" "Farka Toure, Ali" "Cooder, Ry" "Fleet Foxes" "Fleetwood Mac" "Genesis" "Hooker, John Lee" "Jansh, Bert" "Renbourn, John" "Jorge, Seu" "King Crimson" "King, BB" "Kitch" "Led Zeppelin" "Lindisfarne" "Marley, Bob" "Bob Marley and the Wailers" "Mike Oldfield" "Mitchel, Joni" "Moody Blues, The" "Moore, Gary" "Morrison, Van" "Mother's Cake" "Opeth" "Orchestra Baobab" "Phase II" "Pink Floyd" "Porcupine Tree" "Radiohead" "Rodriguez" "Rolling Stones" "Rush" "Santana" "Span, Otis" "Sparrow" "Steely Dan" "Stevens, Cat" "Storm Corrosion" "Thin Lizzy" "Various" "Waller, Fats" "Who, The" "Wilson, Steven" "Wings" "Yes" "Young, Neil" ])

(defn create-record
  [r]
  (let [[a b] (str/split r #", ")]
    {:sortName r :name (if (nil? b)
                         r
                         (str b " " a))}))

(doall (map #(-> % create-record add-artist) artist-vec))