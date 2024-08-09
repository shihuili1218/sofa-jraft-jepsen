(defcluster :jraft
  :clients [{:host "jraft.host1" :user "root"}
            {:host "jraft.host2" :user "root"}
            {:host "jraft.host3" :user "root"}
            {:host "jraft.host4" :user "root"}
            {:host "jraft.host5" :user "root"}])

(deftask :date "echo date on cluster"  []
  (ssh "date"))

(deftask :build []
  (local
   (run
     (cd "atomic-server"
         (run
           "lein clean ; lein uberjar"))))
  (local (run "rm atomic-server.tar.gz; tar zcvf atomic-server.tar.gz atomic-server/target atomic-server/control.sh atomic-server/stop.sh atomic-server/test_server.properties")))


(deftask :deploy []
  (scp "atomic-server.tar.gz" "/home/admin/")
  (ssh
     (run
       (cd "/home/admin"
           (run "rm -rf atomic-server/")
           (run "tar zxvf atomic-server.tar.gz")))))
