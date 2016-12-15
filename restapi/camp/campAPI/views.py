from django.shortcuts import get_object_or_404
from django.db import IntegrityError
from django.contrib.auth.models import User

from rest_framework import viewsets, status
from rest_framework.generics import ListCreateAPIView, ListAPIView
from rest_framework.reverse import reverse
from serializers import UserSerializer, CampGroupSerializer, CampUserSerializer, MembershipGroupSerializer, MembershipUserSerializer

from models import CampUser, CampGroup, Membership
import permissions
from rest_framework.views import APIView
from rest_framework.response import Response

# Currently not used*3
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
    permission_classes = (permissions.CampUserPermission,)

    def retrieve(self, request, pk=None, *args, **kwargs):
        instance = self.get_object()
        groups = Membership.objects.filter(member=instance)
        group_serializer = MembershipGroupSerializer(groups, many=True)
        return Response({"User": self.get_serializer(instance).data, "Groups": group_serializer.data})

    def destroy(self, request, pk=None, *args, **kwargs):
        Membership.objects.filter(member=self.get_object()).delete()
        return viewsets.ModelViewSet.destroy(self, request, pk, args, kwargs)


class SelfViewSet(ListAPIView):
    serializer_class = CampUserSerializer

    def list(self, request, *args, **kwargs):
        instance = CampUser.objects.get(user=request.user)
        groups = Membership.objects.filter(member=instance)
        instance_serializer = CampUserSerializer(instance)
        group_serializer = MembershipGroupSerializer(groups, many=True)
        return Response({"User": instance_serializer.data, "Groups": group_serializer.data})


class CamperViewSet(CampUserViewSet):
    queryset = CampUser.objects.filter(role=CampUser.CAMPER)
    serializer_class = CampUserSerializer


class CounsellorViewSet(CampUserViewSet):
    queryset = CampUser.objects.filter(role=CampUser.COUNSELLOR)
    serializer_class = CampUserSerializer


class AdministratorsViewSet(CampUserViewSet):
    queryset = CampUser.objects.filter(role=CampUser.ADMINISTRATOR)
    serializer_class = CampUserSerializer


class GroupViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = CampGroup.objects.all()
    serializer_class = CampGroupSerializer
    permission_classes = (permissions.CampGroupPermission,)

    def get_queryset(self):
        return CampGroup.objects.filter(id=self.kwargs['pk'])

    def retrieve(self, request, *args, **kwargs):
        instance = self.get_object()
        members = Membership.objects.filter(group=instance)
        members_serializer = MembershipUserSerializer(members, many=True)
        serializer = self.get_serializer(instance)
        return Response({'Group': serializer.data, 'Members': members_serializer.data})

    def create(self, request, pk=None, *args, **kwargs):
        userid = request.data['userid']
        user = get_object_or_404(CampUser.objects.all(), pk=userid)
        response = status.HTTP_201_CREATED
        try:
            Membership.objects.create(member=user, group=self.get_object())
        except IntegrityError as e:
            return Response({"Error": "Unable to add user to group"}, status=status.HTTP_400_BAD_REQUEST)

        # sucessfully added, return a standard post response
        response = Response(status=response)
        response['Location'] = reverse('group-details', kwargs={'pk': pk})
        return response

    def destroy(self, request, pk=None, *args, **kwargs):
        Membership.objects.filter(group=self.get_object()).delete()
        return viewsets.ModelViewSet.destroy(self, request, pk, args, kwargs)


class MembershipViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows groups to be viewed or edited.
    """
    queryset = Membership.objects.all()
    serializer_class = MembershipUserSerializer
    permission_classes = (permissions.CampAdminPermissions,)

    def get_object(self):
        group = get_object_or_404(CampGroup.objects.filter(id=self.kwargs['pk']))
        member = get_object_or_404(CampUser.objects.filter(id=self.kwargs['userid']))
        return Membership.objects.get(group=group, member=member)


class GroupList(ListCreateAPIView):
    queryset = CampGroup.objects.all()
    serializer_class = CampGroupSerializer
    permission_classes = (permissions.CampGroupPermission,)
