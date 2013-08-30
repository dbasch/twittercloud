(ns twittercloud.core
  (:gen-class :main true)
  (:import processing.core.PApplet wordcram.WordCram wordcram.Placers wordcram.text.Text))

;; use the WordCram library to render a word cloud
;; with a hardcoded style and font
(defn draw [text img & opts]
  (let [applet (PApplet.)
        font (.createFont applet (if (not= nil (first opts)) (first opts) "coolvetica/COOLVETI.TTF") 1)
        colors (int-array [(.color applet 204 151 51)
                          (.color applet 102 0 0)
                          (.color applet 151 0 0)
                          (.color applet 204 102 0)
                          (.color applet 10 10 10)])]

    (set! (.g applet) (.createGraphics applet 800 600 PApplet/JAVA2D))
    (.beginDraw (.g applet))
    (.colorMode applet PApplet/RGB)
    (.background applet 255)
    (doto (WordCram. applet)
      (.fromText (Text. text))
      (.withFont font)
      (.withPlacer (Placers/centerClump))
      (.angledBetween 0 0)
      (.withWordPadding 2)
      (.withColors colors)
      (.sizedByWeight 5 140)
      (.drawAll))
    
    (doto (.g applet) (.endDraw) (.save img))
    (println "saved" img)))

;; draw a word cloud for a text file
(defn -main [& args]
  (draw (slurp (first args)) (second args) (nth args 2)))
