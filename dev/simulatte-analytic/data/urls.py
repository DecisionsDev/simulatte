from django.urls import path

from . import views
from .views import get_from_url

urlpatterns = [
    path('from-url/', views.get_from_url, name='get_from_url'),
    path('generate/', views.generate_data, name='generate_data'),
    path('from-csv/', views.generate_data_from_csv, name='generate_data_from_csv'),
]