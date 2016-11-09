from django.shortcuts import render
from django.contrib.auth.models import User, Group
from rest_framework import viewsets, status
from rest_framework.generics import ListCreateAPIView
from serializers import UserSerializer, GroupSerializer, CampUserSerializer
from rest_framework.decorators import detail_route, list_route

from models import CampUser

from rest_framework.views import APIView
from rest_framework.response import Response

from django.http import Http404

# Create your views here.


class CamperViewSet(viewsets.ModelViewSet):
    queryset = CampUser.objects.filter(role=CampUser.CAMPER)
    serializer_class = CampUserSerializer


class CounsellorViewSet(viewsets.ModelViewSet):
    queryset = CampUser.objects.filter(role=CampUser.COUNSELLOR)
    serializer_class = CampUserSerializer


class AdministratorsViewSet(viewsets.ModelViewSet):
    queryset = CampUser.objects.filter(role=CampUser.ADMINISTRATOR)
    serializer_class = CampUserSerializer


class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = User.objects.all()
    serializer_class = UserSerializer


class CampUserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = CampUser.objects.all()
    serializer_class = CampUserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Group.objects.all()
    serializer_class = GroupSerializer

    def get_queryset(self):
        return Group.objects.filter(id=self.kwargs['pk'])

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        campusers = CampUser.objects.filter(user=User.objects.filter(groups=instance))
        campuser_serializer = CampUserSerializer(campusers, many=True)
        serializer = self.get_serializer(instance)
        #print campuser_serializer.data
        #print serializer.data
        return Response({'data': serializer.data, 'users': campuser_serializer.data})

    def create(self, request, pk=None, *args, **kwargs):
       pass

    def update(self, request, pk=None, *args, **kwargs):
        pass

    def destroy(self, request, pk=None, *args, **kwargs):
        pass


class GroupList(ListCreateAPIView):
    queryset = Group.objects.all()
    serializer_class = GroupSerializer


# More detailed implementation of the above

# class CampUserList(APIView):
#     def get(self, request, format=None):
#         users = CampUser.objects.all()
#         serializer = CampUserSerializer(users, many=True)
#         return Response(serializer.data)
#
#     def post(self, request, format=None):
#         serializer = CampUserSerializer(data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#
#
# class CampUserDetail(APIView):
#     def get_object(self, id):
#         try:
#             return CampUser.objects.get(id=id)
#         except CampUser.doesNotExist:
#             raise Http404
#
#     def get(self, request, id, format=None):
#         camp_user = self.get_object(id)
#         serializer = CampUserSerializer(camp_user)
#         return Response(serializer.data)
#
#     def put(self, request, id, format=None):
#         camp_user = self.get_object(id)
#         serializer = CampUserSerializer(camp_user, data=request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
#
#     def delete(self, request, id, format=None):
#         camp_user = self.get_object(id)
#         camp_user.delete()
#         return Response(status=status.HTTP_204_NO_CONTENT)
