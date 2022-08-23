from django.shortcuts import render

# Create your views here.
import json
import papermill as pm
import subprocess
import os
from pathlib import Path

from rest_framework.decorators import api_view

from django.http import HttpResponse


def index(request):
    return HttpResponse("Simulatte is ready !!!.")


@api_view(['POST'])
def compute_notebook(request):
    body_unicode = request.body.decode('utf-8')
    request_body = json.loads(body_unicode)

    uid = request_body["uid"]
    name = request_body["name"]
    notebook_uri = request_body["notebookUri"]
    datasink_uri = request_body["dataSink"]["uri"]

    response = pm.execute_notebook(
        notebook_uri,
        os.path.splitext(notebook_uri)[0] + '_output_{}_{}.ipynb'.format(name, uid),
        parameters=dict(datasink_path=datasink_uri)
    )
    notebook2html(os.path.splitext(notebook_uri)[0] + '_output_{}_{}.ipynb'.format(name, uid))
    return HttpResponse(response)


def notebook2html(filepath):
    cmd = ["jupyter", "nbconvert", "--to", "html", "--no-input", filepath]
    subprocess.run(cmd)


@api_view(['POST'])
def compare_2_runs(request):
    body_unicode = request.body.decode('utf-8')
    request_body = json.loads(body_unicode)

    first_run = json.loads(request_body["firstRun"])
    second_run = json.loads(request_body["secondRun"])

    first_run_uid = first_run["uid"]
    second_run_uid = second_run["uid"]

    notebook_uri = request_body["notebookUri"]

    pm.execute_notebook(
        notebook_uri,
        str(Path(notebook_uri).parent)+'/comparison_output_{}_vs_{}.ipynb'.format(first_run_uid, second_run_uid),
        parameters=dict(first_run=first_run, second_run=second_run)
    )
    notebook2html(str(Path(notebook_uri).parent)+'/comparison_output_{}_vs_{}.ipynb'.format(first_run_uid,
                                                                                            second_run_uid))
    return HttpResponse("{}")
