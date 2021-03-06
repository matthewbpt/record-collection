(ns record-collection.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub subscribe]]
            [clojure.string :refer [blank?]]))


(defn register-flat-sub [s k]
  (register-sub s
                (fn [db _]
                  (reaction (k @db)))))

(register-flat-sub :current-artists :current-artists)

(register-sub
  :albums
  (fn [db [_ {id :id}]]
    (let [albums (reaction (:albums @db))
          filtered-albums (reaction (filter #(contains? (:artists %) id) @albums))]
      (reaction @filtered-albums))))

(register-sub
  :filtered-artists
  (fn [db _]
    (let [items (reaction (:current-artists @db))
          s (reaction (:filter @db))]
      (reaction
        (if (empty? @s)
          (sort-by :sortName @items)
          (sort-by :sortName (filter #(> (.indexOf (-> % :name .toLowerCase) (.toLowerCase @s)) -1) @items)))))))

(register-sub
  :artist
  (fn [db [_ artist-name]]
    (let [items (reaction (:current-artists @db))]
      (reaction (first (filter #(= (:name %) artist-name) @items))))))

(register-sub
  :current-artist
  (fn [db _]
    (let [artist-name (reaction (:current-artist @db))]
      (subscribe [:artist @artist-name]))))