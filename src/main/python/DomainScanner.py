import datetime

def main():
    print("Starting")
    start_time = datetime.datetime.utcnow()
    ZONEFILESDIR = "/home/dan/zonefiles"
    comZonefile = ZONEFILESDIR + "/verisign/com.zone"
    netZonefile = ZONEFILESDIR + "/verisign/net.zone"
    allZoneFiles: List[File] = new File(ZONEFILESDIR + "/icann/").listFiles.toList :+ comZonefile :+ netZonefile


    end_time = datetime.datetime.utcnow()

    elapsed_time = end_time - start_time


    print("Elapsed time: " + str(elapsed_time.total_seconds()))

if __name__ == "__main__":
    main()