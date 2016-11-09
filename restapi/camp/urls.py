"""camp URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.10/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url, include
from django.contrib import admin
from rest_framework import routers
from campAPI import views


router = routers.DefaultRouter()
router.register(r'users', views.CampUserViewSet)
router.register(r'campers', views.CamperViewSet)
router.register(r'counsellors', views.CounsellorViewSet)
router.register(r'administrators', views.AdministratorsViewSet)
#router.register(r'groups/(?P<id>[0-9]+)/$', views.GroupViewSet)

group_detail = views.GroupViewSet.as_view({
    'get': 'retrieve',
    'put': 'update',
    'delete': 'destroy',
    'post': 'create',
})

urlpatterns = [
    # url(r'^admin/', admin.site.urls),
    url(r'^', include(router.urls)),
    url(r'^groups/$', views.GroupList.as_view()),
    url(r'^groups/(?P<pk>[0-9]+)/$', group_detail),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),

]
