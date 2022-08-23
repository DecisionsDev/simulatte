from django.urls import path
from rest_framework.schemas import get_schema_view
from rest_framework_swagger.renderers import OpenAPIRenderer, SwaggerUIRenderer

from . import views
from rest_framework.urlpatterns import format_suffix_patterns
schema_view = get_schema_view(title='Users API', renderer_classes=[OpenAPIRenderer, SwaggerUIRenderer])

urlpatterns = [
    path('docs/', schema_view, name="docs"),
    path('compare-2-runs', views.compare_2_runs, name='compare_2_runs'),
    path('compute-notebook', views.compute_notebook, name='compute_notebook'),
    path('', views.index, name='index'),
]