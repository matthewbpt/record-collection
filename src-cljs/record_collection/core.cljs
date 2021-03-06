(ns record-collection.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [markdown.core :refer [md->html]]
            [ajax.core :refer [GET POST]]
            [record-collection.api :refer [init]]
            [record-collection.views.homepage :refer [artists search]]
            [record-collection.views.artist :refer [artist-view]]
            [record-collection.views.add-artist :refer [add-artist-form]]
            [record-collection.views.add-album :refer [add-album-form]]
            [re-frame.core :refer [dispatch-sync]] )
  (:import goog.History))


(defn nav-link [uri title page collapsed?]
  [:li {:class (when (= page (session/get :page)) "active")}
   [:a {:href uri
        :on-click #(reset! collapsed? true)}
    title]])

(defn navbar []
  (let [collapsed? (atom true)]
    (fn []
      [:nav.navbar.navbar-inverse.navbar-fixed-top
       [:div.container
        [:div.navbar-header
         [:button.navbar-toggle
          {:class         (when-not @collapsed? "collapsed")
           :data-toggle   "collapse"
           :aria-expanded @collapsed?
           :aria-controls "navbar"
           :on-click      #(swap! collapsed? not)}
          [:span.sr-only "Toggle Navigation"]
          [:span.icon-bar]
          [:span.icon-bar]
          [:span.icon-bar]]
         [:a.navbar-brand {:href "#/"} "record-collection"]]
        [:div.navbar-collapse.collapse
         (when-not @collapsed? {:class "in"})
         [:ul.nav.navbar-nav
          [nav-link "#/" "Home" :home collapsed?]
          [nav-link "#/artist/add" "Add Artist" :add-artist collapsed?]
          [nav-link "#/album/add" "Add Album" :add-album collapsed?]
          [nav-link "#/about" "About" :about collapsed?]]]]])))

(defn about-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     "this is the story of record-collection... work in progress"]]])

(defn home-page []
  [:div.container
   [search :filter]
   [artists]])

(defn artist-page []
  [:div.container
   [artist-view]])

(defn add-artist-page []
  [:div.container
   [add-artist-form]])

(defn add-album-page []
  [:div.container
   [add-album-form]])

(def pages
  {:home #'home-page
   :about #'about-page
   :artist #'artist-page
   :add-artist #'add-artist-page
   :add-album #'add-album-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "/artist/add" []
  (session/put! :page :add-artist))

(secretary/defroute "/album/add" []
  (session/put! :page :add-album))

(secretary/defroute "/artist/:artist" [artist]
  (dispatch-sync [:current-artist artist])
  (session/put! :page :artist))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          EventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

(init)

;; -------------------------
;; Initialize app
(defn mount-components []
  (reagent/render [#'navbar] (.getElementById js/document "navbar"))
  (reagent/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-components))
