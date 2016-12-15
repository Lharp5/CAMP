from rest_framework import permissions
from models import CampUser, Membership
from django.shortcuts import get_object_or_404, get_list_or_404

def getCampUser(user):
    user = get_object_or_404(CampUser.objects.filter(user=user))
    return user


class CampUserPermission(permissions.BasePermission):
    def has_permission(self, request, view):
        user = getCampUser(request.user)

        if user.role == CampUser.COUNSELLOR and request.method in permissions.SAFE_METHODS:
            return True

        if user.role == CampUser.ADMINISTRATOR:
            return True

        #  If we are a user then continue to the has_object_permission
        if view.action in ['retrieve', 'put', 'update', 'partial_update', 'patch']:
            return True

        return False

    def has_object_permission(self, request, view, obj):
        user = getCampUser(request.user)

        if user.role == CampUser.ADMINISTRATOR or obj == user:
            return True

        if user.role == CampUser.COUNSELLOR and request.method in permissions.SAFE_METHODS:
            user_groups = list()
            for membership in list(Membership.objects.filter(member=user)):
                user_groups.append(membership.group)

            obj_groups = list()
            for membership in list(Membership.objects.filter(member=obj)):
                obj_groups.append(membership.group)

            # Finding the interaction
            common_groups = list(set(user_groups) & set(obj_groups))
            if len(common_groups) > 0:
                return True

        return False


class CampGroupPermission(permissions.BasePermission):
    def has_permission(self, request, view):
        user = getCampUser(request.user)

        if user.role == CampUser.ADMINISTRATOR:
            return True

        #  If we are a user then continue to the has_object_permission
        try:  # This check is for viewsets, if we are calling it on an API view it will not work.
            if view.action in ['retrieve', 'put', 'update', 'partial_update', 'patch']:
                return True
        except AttributeError:  # Since we cannot check an API view, only allow safe methods
            if user.role == CampUser.COUNSELLOR and request.method in permissions.SAFE_METHODS:
                return True

        return False

    def has_object_permission(self, request, view, obj):
        user = getCampUser(request.user)
        membership = Membership.objects.filter(member=user, group=obj)
        if user.role == CampUser.ADMINISTRATOR:
            return True
        elif membership:
            if user.role == CampUser.COUNSELLOR and request.method in ['PUT', 'PATCH']:
                return True
            if request.method in permissions.SAFE_METHODS:
                return True

        return False


class CampAdminPermissions(permissions.BasePermission):
    def has_permission(self, request, view):
        user = getCampUser(request.user)
        if user.role == CampUser.ADMINISTRATOR:
            return True
        return False
