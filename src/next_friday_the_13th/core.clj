(ns next-friday-the-13th.core
  (:import [java.util Calendar GregorianCalendar TimeZone])
  (:use [noir.server :only [start]]
        [noir.core :only [defpartial defpage]]
        [noir.response :only [json jsonp]]
        [noir.statuses :only [set-page!]]
        [hiccup.page-helpers :only [html5]]))

(def limit 9999)
(def site-name "次の13日の金曜日")
(def font-size 14)

(defn- within-the-limit? [n]
  (> n limit))

(defn- get-today []
  (GregorianCalendar. (TimeZone/getTimeZone "Asia/Tokyo")))

(defn- after-dates-infinity [date]
  (iterate #(doto % (.add Calendar/DAY_OF_MONTH 1)) date))

(defn- filter-the-13th-infinity [days]
  (filter #(= (.get % Calendar/DAY_OF_MONTH) 13) days))

(defn- filter-friday-infinity [days]
  (filter #(= (.get % Calendar/DAY_OF_WEEK) Calendar/FRIDAY) days))

(defn- map-format-days [days]
  (map #(format "%d-%02d-%02d" (.get % Calendar/YEAR) (inc (.get % Calendar/MONTH)) (.get % Calendar/DAY_OF_MONTH)) days))

(defn- filter-friday-the-13th-infinity [date]
  (->> date 
    (after-dates-infinity)
    (filter-the-13th-infinity)
    (filter-friday-infinity)
    (map-format-days)))

(defn- get-next-friday-the-13th [& {:keys [n] :or {n 1}}]
  (take n (filter-friday-the-13th-infinity (get-today)))) 

(defn- formatted-get-next-friday-the-13th [& {:keys [n] :or {n 1}}]
  (apply str (interpose "<br />" (get-next-friday-the-13th :n n))))

(defn- numberchar? [ch]
  (and (<= (int \0) (int ch)) (>= (int \9) (int ch))))

(defn- numberstring? [string]
  (every? numberchar? string))

(defpartial layout [title & content]
  (html5
    [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "format-detection" :content "telephone=no"}]
      [:title title]
      [:style (str
                (format "body { font-size: %dpx; font-family: monospace; }" font-size)
                "p { margin: 8px 0; }"
                ".howto { color: #000; }"
                "ul { margin: 0; padding: 0; }"
                "li { display: inline; list-style-type: none; margin: 0 1em 0 0; }")]
      [:script (str
                 "var _gaq = _gaq || [];"
                 "_gaq.push(['_setAccount', 'UA-28378627-9']);"
                 "_gaq.push(['_trackPageview']);"
                 "(function() {"
                 "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;"
                 "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';"
                 "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);"
                 "})();")]]
    [:body
      [:p
         (interpose " " [
           "URL指定の例:"
           [:a.howto {:href "/500"} "500日分の次の13日の金曜日"]
           [:a.howto {:href "/10/json"} "10日分の次の13日の金曜日のJSON"]
           [:a.howto {:href "/6/jsonp"} "6日分の次の13日の金曜日のJSONP (callback=callback)"]
           [:a.howto {:href "/7/jsonp?callback=hoge"} "7日分の次の13日の金曜日のJSONP (callback=hoge)"]])]
      [:p content]
      [:p
         [:a.howto {:href "https://github.com/taiju/next-friday-the-13th#%E4%BD%BF%E3%81%84%E6%96%B9%EF%BC%88%E3%83%91%E3%83%A9%E3%83%A1%E3%83%BC%E3%82%BF%E3%81%AE%E4%BB%95%E6%A7%98"} "使い方"]]]))

(defpage "/" []
  (layout site-name (formatted-get-next-friday-the-13th)))

(defpage "/:n" {:keys [n]}
  (if (numberstring? n)
    (if (within-the-limit? (Integer/parseInt n))
      (layout (format "%s - over limit %d日分" site-name limit) [:b (format "%d日分で勘弁してください。" limit)] [:br] (formatted-get-next-friday-the-13th :n limit))
      (layout (format "%s - %s日分" site-name n) (formatted-get-next-friday-the-13th :n (Integer/parseInt n))))
    (layout (format "%s - invalid parameter" site-name) "パラメータには数字しか受け付けません。")))

(defpage "/:n/json" {:keys [n]}
  (if (numberstring? n)
    (if (within-the-limit? (Integer/parseInt n))
      (json (get-next-friday-the-13th :n 999))
      (json (get-next-friday-the-13th :n (Integer/parseInt n))))
    (json {:error "Non-numeric parameters don't allow"})))

(defpage "/:n/jsonp" {:keys [n callback] :or {callback "callback"}}
  (if (numberstring? n)
    (if (within-the-limit? (Integer/parseInt n))
      (jsonp callback (get-next-friday-the-13th :n 999))
      (jsonp callback (get-next-friday-the-13th :n (Integer/parseInt n))))
    (jsonp callback {:error "Non-numeric parameters don't allow"})))

(set-page! 404 (layout (format "%s - 404 not found" site-name) "404 not found."))

(defn -main [port]
  (start (Integer/parseInt port)))
