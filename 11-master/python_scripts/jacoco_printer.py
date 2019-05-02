# Expected input: jacoco "index.html" report

import sys
import os
from html.parser import HTMLParser
from enum import Enum


def main():
    if len(sys.argv) != 2:
        print("Invalid launch arguments: " + " ".join(sys.argv))
        print("\nUsage:")
        print("jacoco_printer.py /path/to/index.html")
        sys.exit(0)
    jacoco_file = sys.argv[1]
    if not os.path.isfile(jacoco_file):
        print("Invalid file: {}".format(jacoco_file))
    
    # jacoco file exists, attempt to parse it
    with open(jacoco_file) as f:
        parser = MyHTMLParser()
        parser.feed(f.read())
        
        # Move "Total" row to end
        if len(parser.rows) >= 3:
            parser.rows.append(parser.rows.pop(1))
        
        # Rename columns "Missed Instructions" and "Missed Branches"
        if len(parser.rows) > 0:
            parser.rows[0][1] = "Tested Inst."
            parser.rows[0][3] = "Tested Branches"
        
        # Only keep first 5 columns for compactness
        for i, row in enumerate(parser.rows):
            parser.rows[i] = row[:5]

        print(parser.h1)
        cell_widths = [8 for e in range(len(parser.rows[0]))]
        for row in parser.rows:
            for i, e in enumerate(row):
                if len(e) + 2 > cell_widths[i]:
                    cell_widths[i] = len(e) + 2
        for row in parser.rows:
            for i, val in enumerate(row):
                assert isinstance(val, str)
                if i == 0:
                    row[i] = val.ljust(cell_widths[i])
                else:
                    row[i] = val.center(cell_widths[i])
            print("|".join(row))
    print("\nGoodbye.")


def count():
    cnt = 0
    while True:
        yield cnt
        cnt += 1


unique_number = count()


class RecordingState(Enum):
    NONE = next(unique_number)
    H1 = next(unique_number)
    ROW = next(unique_number)


class MyHTMLParser(HTMLParser):
    TAG_H1 = "h1"
    TAG_TR = "tr"
    TAG_IMG = "img"
    BAR_RED = "jacoco-resources/redbar.gif"
    BAR_GREEN = "jacoco-resources/greenbar.gif"
    
    def __init__(self):
        self.recordState = RecordingState.NONE
        self.red_amount = -1
        self.h1 = ""
        self.rows = list()
        super().__init__()
    
    def handle_starttag(self, tag, attrs):
        if tag == MyHTMLParser.TAG_H1:
            self.recordState = RecordingState.H1
        elif tag == MyHTMLParser.TAG_TR:
            self.rows.append(list())
            self.recordState = RecordingState.ROW
        elif tag == MyHTMLParser.TAG_IMG and self.recordState == RecordingState.ROW:
            # jacoco uses image bars to indicate tested to untested ratio, parse them into "n of x" string
            attrs_dict = dict()
            for t in attrs:
                attrs_dict[t[0]] = t[1]
            if "src" in attrs_dict:
                if attrs_dict["src"] == MyHTMLParser.BAR_RED:
                    self.red_amount = attrs_dict["title"]
                    self.red_amount = self.red_amount.replace(",", "")
                    self.red_amount = self.red_amount.replace(".", "")
                elif attrs_dict["src"] == MyHTMLParser.BAR_GREEN:
                    green_amount = attrs_dict["title"]
                    green_amount = green_amount.replace(",", "")
                    green_amount = green_amount.replace(".", "")
                    total_amount = int(green_amount) + int(self.red_amount)
                    self.rows[-1].append("{} of {}".format(green_amount, str(total_amount)))
    
    def handle_endtag(self, tag):
        if tag == MyHTMLParser.TAG_H1:
            self.recordState = RecordingState.NONE
        elif tag == MyHTMLParser.TAG_TR:
            self.recordState = RecordingState.NONE
    
    def handle_data(self, data):
        if self.recordState == RecordingState.NONE:
            return
        elif self.recordState == RecordingState.H1:
            self.h1 = data
        elif self.recordState == RecordingState.ROW:
            self.rows[-1].append(data)
    
    def error(self, message):
        print("Parser error:")
        print(message)


if __name__ == "__main__":
    main()

