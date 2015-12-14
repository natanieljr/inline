"""

Change log:

14 Oct 2015 -- by Konrad Jamrozik -- jamrozik@st.cs.uni-saarland.de
  Changed default param values to point to my scratch dir.

-----

Script to extract the top applications of each category in a specific date.

Usage example:
 python getTopAppsPerCategory.py --date=2014-02-01 --path=/jacobs/APKs --top=100 --outdir=/home/gorla  --rankType="Top Free"

Such execution would create a file "topApps-2014-02-02-100-Top Free.txt" in directory /home/gorla containing the 100 Top Free
apps of each category in date 2014-02-01. The path parameter specifies where to find the whole dataset of apps
(on the exsyst machine it is /jacobs/APKs )

author: Alessandra Gorla -- gorla@st.cs.uni-saarland.de
"""

import fnmatch, string, os, re
import sys
from optparse import OptionParser
import logging

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
logging.basicConfig(filename='getTopApps.log',level=logging.WARNING)

## main method to visit the directory and extract information
def getAPPsInfo():
    print("** Extracting the top "+str(top)+" apps from "+path+" for date "+date+" and ranking type "+rankType)
    listApps = []
    #browse in the path directory

    for currentDir, dirs, files in os.walk(path):
        # when you find an apk file, get the apk name and the info in
        # file rank-info.json
        for apk_name in fnmatch.filter(files, "*.apk"):
            rank_file_name = os.path.join(currentDir, "rank-info.json")
            details_file_name = os.path.join(currentDir, "details.json")
            if not os.path.exists(rank_file_name):
                logging.critical(rank_file_name+" does not exist!!!   ")
                continue
            if not os.path.exists(details_file_name):
                logging.critical(details_file_name+" does not exist!!!   ")
                continue
            category = extract_app_category(details_file_name)
            ranking = extract_app_ranking(rank_file_name, category)
            if(ranking == ""):
                # the app is not ranked for that date
                continue
            if(int(ranking) > top):
                continue
            a = APP(os.path.join(currentDir,apk_name), category, ranking)
            listApps.append(a)
            logging.debug("Processed "+apk_name + " " + ranking + " " + category)
    printTopApps(listApps)

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
        logging.warning("File "+detailsFile+" does not have a category!")
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
    parser.add_option("--outdir", action="store", type="string", dest="target_outdir", help="output dir", metavar="DIR", default="/scratch/jamrozik/")
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
    getAPPsInfo()
