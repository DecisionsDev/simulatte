from pathlib import Path
import numpy as np

from django.shortcuts import render

# Create your views here.

from django.http import HttpResponse
import urllib.request
import pandas as pd
from faker import Faker

import json

from rest_framework.decorators import api_view


@api_view(['POST'])
def get_from_url(request):
    body_unicode = request.body.decode('utf-8')
    request_body = json.loads(body_unicode)

    data_source_url = request_body["data_source_url"]
    data_sink_uri = request_body["data_sink_uri"]
    data_sink_size = request_body["data_sink_size"]

    response = "Ready to get data from : {} et print in {}".format(data_source_url, data_sink_uri)

    fake = Faker()

    # Models
    borrower = dict()
    loan = dict()
    loanRequest = dict()

    # 0 = name, 1 = creditScore, 2 = income, 3 = loanAmount, 4 = monthDuration, 5 = rate, 6 = approval,
    # 7 = yearlyReimbursement

    # credit score between 300 and 850
    # yearlyIncome 10 000 and 3 000 000
    # amount 1 000 and 1 200 000
    # duration 1 and 20*12
    # rate 0 et 0.1
    # yearly_reimbursement : internet

    # fake.name()
    file = Path(data_sink_uri)
    if file.exists():
        file = open(data_sink_uri, "r+")
        file.truncate(0)
        file.close()

    skip_header = True
    counter = 0
    start_point = 0
    for line in urllib.request.urlopen(data_source_url):
        if not skip_header and counter > start_point:
            line = line.rstrip().decode('utf-8').split(',')

            borrower["name"] = fake.name()
            borrower["creditScore"] = int(line[1].strip())
            borrower["yearlyIncome"] = int(line[2].strip())

            loan["amount"] = int(line[3].strip())
            loan["duration"] = int(line[4].strip())
            loan["yearlyInterestRate"] = float(line[5].strip())
            loan["yearlyRepayment"] = int(line[7].strip())

            loanRequest["borrower"] = borrower
            loanRequest["loan"] = loan

            with open(data_sink_uri, 'a') as file:
                file.write(json.dumps(loanRequest))
                file.write('\n')

        skip_header = False
        if counter == start_point+data_sink_size: break
        counter += 1

    return HttpResponse(response)


@api_view(['POST'])
def generate_data(request):
    body_unicode = request.body.decode('utf-8')
    request_body = json.loads(body_unicode)
    # print("REQUEST BODY : ", request_body)

    # dist_list = ['uniform','normal','exponential','lognormal','chisquare','beta']
    # param_list = ['-1,1', '0,1', '1', '0,1', '2', '0.5,0.9']

    seed = 11
    rand = np.random.RandomState(seed)

    data = pd.DataFrame([])
    for key, value in request_body["fields"].items():
        expression = 'rand.' + value + '(' + '0.1' + ',20)'
        print("expression : ", expression)
        temp_values = eval(expression)
        print("column values : ", temp_values)
        data[key] = temp_values

    print("DATASET : ", data)

    response = "Done !"
    return HttpResponse(response)


@api_view(['POST'])
def generate_data_from_csv(request):
    body_unicode = request.body.decode('utf-8')
    request_body = json.loads(body_unicode)

    data_uri = request_body["data_uri"]

    # Create dataframe from uploaded file
    raw_data = pd.read_csv(filepath_or_buffer=data_uri, delimiter=',')

    # to be clean after
    # raw_data['approval'] = raw_data['rate']
    # raw_data['rate'] = raw_data['approval']

    print("INITIAL DATA : ", raw_data)
    formatted_data = pd.DataFrame([])
    for key, value in request_body['fields'].items():
        formatted_data[value] = raw_data[key].values

    filepath = Path("/Users/tiemokodembele/Documents/internShip/simulatte/jupyter_client/testGeneration.csv")

    print("DATA : ", formatted_data)

    # pandas.DataFrame.to_csv('your_file_name', index=False)

    response = "Task done !"
    return HttpResponse(response)
