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
from rest_framework_jwt.views import obtain_jwt_token


router = routers.DefaultRouter()
router.register(r'users', views.CampUserViewSet)
router.register(r'campers', views.CamperViewSet)
router.register(r'counsellors', views.CounsellorViewSet)
router.register(r'administrators', views.AdministratorsViewSet)

group_detail = views.GroupViewSet.as_view({
   'get': 'retrieve',
   'post': 'create',
   'put': 'update',
   'delete': 'destroy',

})

membership_delete = views.MembershipViewSet.as_view({
    'delete': 'destroy',
})

urlpatterns = [
    # url(r'^admin/', admin.site.urls),
    url(r'^', include(router.urls)),
    url(r'^self/$', views.SelfViewSet.as_view(), name='self'),
    url(r'^groups/$', views.GroupList.as_view(), name='groups'),
    url(r'^groups/(?P<pk>[0-9]+)/$', group_detail, name='group-details'),
    url(r'^groups/(?P<pk>[0-9]+)/(?P<userid>[0-9]+)/$', membership_delete, name='group-details'),
    url(r'^api-token-auth/$', obtain_jwt_token),
]
