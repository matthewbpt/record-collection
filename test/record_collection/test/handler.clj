(ns record-collection.test.handler
  (:require [expectations :refer :all]
            [ring.mock.request :refer :all]
            [record-collection.handler :refer :all]))

(expect {:status 200} (in (app (request :get "/"))))

(expect {:status 404} (in (app (request :get "invalid"))))

(expect {:status 200} (in (app (request :get "/api/artists"))))

;(deftest test-app
;  (testing "main route"
;    (let [response (app (request :get "/"))]
;      (is (= 200 (:status response)))))
;
;  (testing "not-found route"
;    (let [response (app (request :get "/invalid"))]
;      (is (= 404 (:status response))))))
