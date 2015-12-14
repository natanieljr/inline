import fnmatch, string, subprocess, os, re, subprocess
import sys
import glob
from optparse import OptionParser
from multiprocessing import Lock, Process, Queue, current_process

# enumeration for ranking type
class RankingType:
    TOP_NEW_FREE = "Top New Free"
    TOP_FREE = "Top Free"

# class to represent an APP
class APP():
    def __init__(self, name, category, ranking):
        self.name = name
        self.category = category
        self.ranking = ranking

# dictionary with mapping of the categories between 
# ranking json file and description file
categories = {
  "ARCADE" : "Arcade & Action",
  "BOOKS_AND_REFERENCE" : "Books & Reference",
  "BRAIN" : "Brain & Puzzle",
  "BUSINESS" : "Business",
  "CARDS" : "Cards & Casino",
  "CASUAL" : "Casual",
  "COMICS" : "Comics",
  "COMMUNICATION" : "Communication",
  "EDUCATION" : "Education",
  "ENTERTAINMENT" : "Entertainment",
  "FINANCE" : "Finance",
  "HEALTH_AND_FITNESS" : "Health & Fitness",
  "LIBRARIES_AND_DEMO" : "Libraries & Demo",
  "LIFESTYLE" : "Lifestyle",
  "MEDIA_AND_VIDEO" : "Media & Video",
  "MEDICAL" : "Medical",
  "MUSIC_AND_AUDIO" : "Music & Audio",
  "NEWS_AND_MAGAZINES" : "News & Magazines",
  "PERSONALIZATION" : "Personalization",
  "PHOTOGRAPHY" : "Photography",
  "PRODUCTIVITY" : "Productivity",
  "RACING" : "Racing",
  "SHOPPING" : "Shopping",
  "SOCIAL" : "Social",
  "SPORTS" : "Sports",
  "SPORTS_GAMES" : "Sports Games",
  "TOOLS" : "Tools",
  "TRANSPORTATION" : "Transportation",
  "TRAVEL_AND_LOCAL" : "Travel & Local",
  "WEATHER" : "Weather"
}

path = ""
date = ""
top = 100
outdir = ""
rankType = RankingType.TOP_FREE
workers = 4

#toanalyze_queue = Queue()
#analyzedApps_queue = Queue()

## Gets the list of directories (i.e. jobs) to be analyzed
def get_list_dirs_to_analyze():
    listJobs = Queue()
    #browse in the path directory
    for currentDir, dirs, files in os.walk(path):
        # when you find an apk file, add the direcory to the list of jobs
        for apk_name in fnmatch.filter(files, "*.apk"): 
            rank_file_name = os.path.join(currentDir, "rank-info.json")
            details_file_name = os.path.join(currentDir, "details.json")
            if not os.path.exists(rank_file_name):
                print (rank_file_name+" does not exist!!!   ")
                continue
            if not os.path.exists(details_file_name):
                print (details_file_name+" does not exist!!!   ")
                continue
            # if all files are there, add directory to the job list
            listJobs.put(currentDir)
    listJobs.put("STOP")
    return listJobs

## Analyze a directory containing an apk file
def analyze_apk_path(toanalyze_queue, analyzedApps_queue):
   for d in iter(toanalyze_queue.get, "STOP"):
       files = glob.glob(d+'/*.apk')
       # if this assertion fails it means that for some weird reason there used to be an apk
       # file and now there is no apk file anymore in the d directory.
       assert(len(files) == 1)
       apk_name = files[0] 
       rank_file_name = os.path.join(d, "rank-info.json")
       details_file_name = os.path.join(d, "details.json")
       category = extract_app_category(details_file_name)
       ranking = extract_app_ranking(rank_file_name, category)
       if(ranking == ""):
           # the app is not ranked for that date
           continue 
       if(int(ranking) > top):
           continue
       a = APP(os.path.join(d,apk_name), category, ranking)
#       analyzedApps_queue.put(a)
       print(apk_name + " " + ranking + " " + category)

## main method to extract information about ranking and categories
def get_all_APPs_info():
    print("** Extracting the top "+str(top)+" apps from "+path+" for date "+date+" and ranking type "+rankType)

    toanalyze_queue = get_list_dirs_to_analyze()
    analyzed_queue = Queue()
    processes = []

    for w in xrange(workers):
        p = Process(target=analyze_apk_path, args=(toanalyze_queue, analyzed_queue))
        p.start()
        processes.append(p)

    for p in processes:
        p.join()

#    analyzed_queue.put('STOP')

#    for status in iter(done_queue.get, 'STOP'):
#        print status

#    printTopApps(listApps)

## Print all top applications
def printTopApps(listApps):
    outfile = "topApps-"+date+"-"+str(top)+"-"+rankType+".txt"
    out_file_name = os.path.join(outdir, outfile)
    outf = open (out_file_name, 'a')
    for a in listApps:
        outf.write (a.ranking + " ** " + a.category + " ** " + a.name +"\n")
    outf.close()
    print("** Output file name: "+out_file_name)

## Returns the APP category, or NO_CATEGORY if no category is found in the description file
def extract_app_category(detailsFile):
    f = open (detailsFile)
    text = f.read()
    f.close()
    apps_category = re.search ('"appCategory":(.*)"(\s*)],', text, re.DOTALL)
    if apps_category:
        app_cat = apps_category.group().replace('"appCategory": [', "")
        app_cat = app_cat.replace("\"", "")
        app_cat = app_cat.replace("],", "")
        app_cat = app_cat.replace("\n", "")
        app_cat = app_cat.replace(" ", "")
    else:
        print ("** WARNING! File "+detailsFile+" does not have a category!")
        app_cat = "NO_CATEGORY"
    return app_cat

## Return the APP ranking (for the first category of the specified date)
def extract_app_ranking(rankFile, cat):    
    f = open(rankFile)
    text = f.read()
    f.close()
    str = re.search (date+'":(.*}),', text, re.DOTALL)
    # if not match it means that app did not appear in the ranking 
    # in that date, thus skip
    if not str:
        return ""
    for l in str.group(1).splitlines():
        # line should contain the right ranking (e.g. Top Free)
        if rankType in l:
            # and should contain the right category
            if categories[cat] in l:
                app_ranking = re.sub(r'\D', "", l)
                return app_ranking
    # if did not return than the app was in ranking but for another rankType
    return ""

## ------------- parse command line args ----------------##
def parse_command_line_args(command_line_args):
    global path
    global date
    global top
    global outdir
    global rankType

    parser = OptionParser()
    parser.add_option("--path", action="store", type="string", dest="target_path", help="path containing all the APKs", metavar="DIR", default="/jacobs/APKs") 
    parser.add_option("--date", action="store", type="string", dest="target_date", help="date in the following format: 2014-01-22", metavar="DATE", default="2014-01-22")
    parser.add_option("--top", action="store", type="int", dest="target_top", help="limit the number of apps according to their ranking position (top 10, top 100...)", metavar="NUM", default=100)
    parser.add_option("--outdir", action="store", type="string", dest="target_outdir", help="output dir", metavar="DIR", default="/scratch/gorla/")
    parser.add_option("--rankType", action="store", type="string", dest="target_ranktype", help="type of ranking", metavar="'Top Free'|'Top New Free'", default=RankingType.TOP_FREE)

    (options, args) = parser.parse_args(args=command_line_args)
    
    path = options.target_path
    date = options.target_date
    top = options.target_top
    outdir = options.target_outdir
    rankType = options.target_ranktype


## ------------ Main method -------------- ##
if __name__ == '__main__':
    parse_command_line_args(sys.argv[1:])
    get_all_APPs_info()
